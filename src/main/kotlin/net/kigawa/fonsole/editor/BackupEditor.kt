package net.kigawa.fonsole.editor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.singleOrNull
import net.kigawa.fonsole.config.ProjectConfig
import net.kigawa.fonsole.document.BackupDocument
import net.kigawa.fonsole.model.DirectoryModel
import net.kigawa.fonsole.model.FileModel
import net.kigawa.fonsole.mongo.Database
import net.kigawa.kutil.domain.result.ErrorResult
import net.kigawa.kutil.domain.result.Result
import net.kigawa.kutil.domain.result.SuccessResult
import org.bson.types.ObjectId
import org.slf4j.Logger
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import kotlin.io.path.*

class BackupEditor(
    private val projectConfig: ProjectConfig,
    private val logger: Logger,
    database: Database,
) {
    private val collection = database.getCollection(BackupDocument::class)
    private val bucket = database.createBucket(BackupDocument::class.simpleName!!)

    private suspend fun uploadDirectory(currentDirectory: Path): Deferred<DirectoryModel> {
        val files = currentDirectory.listDirectoryEntries().filter { !it.isDirectory() }.map { uploadFile(it) }
        val dirs = currentDirectory.listDirectoryEntries().filter { it.isDirectory() }.map { uploadDirectory(it) }
        return CoroutineScope(currentCoroutineContext()).async {
            DirectoryModel(
                name = currentDirectory.name,
                files = files.map { it.await() },
                subDirectories = dirs.map { it.await() }
            )
        }
    }

    private suspend fun uploadFile(file: Path): Deferred<FileModel> {
        return CoroutineScope(currentCoroutineContext()).async {
            logger.debug("open file {}", file)
            Files.newByteChannel(file).use {
                val uploadSubscriber = ChannelSubscriber<ObjectId>(logger, capacity = 1)
                logger.info("upload file ${file.name}")
                val fileReadPublisher = FileReadPublisher(it,logger)
                bucket.uploadFromPublisher(file.name, fileReadPublisher).subscribe(uploadSubscriber)
                withContext(Dispatchers.IO) {
                    fileReadPublisher.write()
                }
                FileModel(uploadSubscriber.receive(), file.name).also {
                    uploadSubscriber.join()
                }
            }
        }
    }

    suspend fun uploadBackup(backupDocumentId: ObjectId): Result<Unit, Unit> {
        val dir = projectConfig.directory
        if (Files.isDirectory(dir).not()) {
            logger.error("$dir is not directory")
            return ErrorResult(Unit)
        }
        val directory = uploadDirectory(dir).await()
        logger.debug("upload directory {} {}", backupDocumentId, directory)
        collection.findOneAndUpdate(
            Filters.eq("_id", backupDocumentId),
            Updates.combine(
                Updates.set(BackupDocument::rootDirectory.name, directory),
                Updates.set(BackupDocument::endDate.name, LocalDateTime.now()),
            )
        ).also { logger.debug("update root directory {}", it) }
        return SuccessResult(Unit)
    }

    suspend fun insertBackupDocument(backupDocumentId: ObjectId) {
        collection.insertOne(
            BackupDocument(
                id = backupDocumentId,
                startDate = LocalDateTime.now()
            )
        )
    }

    suspend fun findBackup(
        date: LocalDateTime, backups: List<ObjectId>,
    ): Result<BackupDocument, Unit> {
        val document = collection.find(
            Filters.and(
                Filters.lt<LocalDateTime>(BackupDocument::endDate.name, date),
                Filters.`in`<ObjectId>("_id", backups)
            ),
        ).sort(Sorts.descending(BackupDocument::endDate.name))
            .limit(1)
            .singleOrNull()
        if (document == null) {
            logger.error("backup is not exist")
            return ErrorResult(Unit)
        }
        return SuccessResult(document)
    }

    suspend fun downloadBackup(backupDocument: BackupDocument): Result<Unit, Unit> {
        @OptIn(ExperimentalPathApi::class)
        if (projectConfig.directory.exists())
            projectConfig.directory.listDirectoryEntries().forEach { it.deleteRecursively() }
        val root = backupDocument.rootDirectory
        if (root == null) return ErrorResult(Unit)
        val result = downloadDirectory(root, projectConfig.directory)
        if (result.await() is ErrorResult) result.cancelAndJoin()
        return result.await()
    }

    suspend fun downloadDirectory(directoryModel: DirectoryModel, directory: Path): Deferred<Result<Unit, Unit>> {
        return CoroutineScope(currentCoroutineContext()).async {
            directory.createDirectories()
            val dirs = directoryModel.subDirectories.map { downloadDirectory(it, directory.resolve(it.name)) }
            val files = directoryModel.files.map { downloadFile(it, directory.resolve(it.name)) }
            dirs.forEach { if (it.await() is ErrorResult) return@async ErrorResult(Unit) }
            files.forEach { if (it.await() is ErrorResult) return@async ErrorResult(Unit) }
            SuccessResult(Unit)
        }
    }

    suspend fun downloadFile(fileModel: FileModel, file: Path): Deferred<Result<Unit, Unit>> {
        return CoroutineScope(currentCoroutineContext()).async {
            val subscriber = ChannelSubscriber<ByteBuffer>(logger)
            val publisher = bucket.downloadToPublisher(fileModel.id)
            publisher.subscribe(subscriber)
            file.createFile()
            Files.newByteChannel(file.toAbsolutePath(), StandardOpenOption.WRITE).use {
                withContext(Dispatchers.IO) {
                    for (byteBuffer in subscriber) {
                        it.write(byteBuffer)
                    }
                }
            }
            logger.info("created file ${file.name}")
            SuccessResult(Unit)
        }
    }
}