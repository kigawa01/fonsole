package net.kigawa.fonsole.editor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import net.kigawa.fonsole.backup.BackupConfig
import net.kigawa.fonsole.document.BackupDocument
import net.kigawa.fonsole.model.Database
import net.kigawa.fonsole.model.Directory
import net.kigawa.fonsole.model.FileModel
import net.kigawa.kutil.domain.result.ErrorResult
import net.kigawa.kutil.domain.result.Result
import net.kigawa.kutil.domain.result.SuccessResult
import org.bson.types.ObjectId
import org.reactivestreams.Subscriber
import org.slf4j.Logger
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime

class BackupEditor(
    private val backupConfig: BackupConfig,
    private val logger: Logger,
    database: Database,
) {
    private val collection = database.getCollection(BackupDocument::class)
    private val bucket = database.createBucket(BackupDocument::class.simpleName!!)

    private suspend fun uploadDirectory(currentDirectory: File): Deferred<Directory> {
        val files = currentDirectory.listFiles { !it.isDirectory }.map { uploadFile(it) }
        val dirs = currentDirectory.listFiles { it.isDirectory }.map { uploadDirectory(it) }
        return CoroutineScope(currentCoroutineContext()).async {
            Directory(
                name = currentDirectory.name,
                files = files.map { FileModel(it.await()) },
                subDirectories = dirs.map { it.await() }
            )
        }
    }

    private suspend fun uploadFile(file: File): Deferred<ObjectId> {
        return CoroutineScope(currentCoroutineContext()).async {
            Files.newByteChannel(file.toPath()).use {
                val channel = Channel<ObjectId>(capacity = 1)
                val uploadSubscriber: Subscriber<ObjectId> = ChannelReceiveSubscriber(channel, logger)
                logger.info("upload file ${file.name}")
                val fileReadPublisher = FileReadPublisher(it)
                bucket.uploadFromPublisher(file.name, fileReadPublisher).subscribe(uploadSubscriber)
                fileReadPublisher.write()
                channel.receive()
            }
        }
    }

    suspend fun uploadBackup(backupDocumentId: ObjectId): Result<Unit, Unit> {
        val dir = backupConfig.directory
        if (dir.isDirectory.not()) {
            logger.error("$dir is not directory")
            return ErrorResult(Unit)
        }
        val directory = uploadDirectory(dir).await()
        collection.findOneAndUpdate(
            Filters.eq(BackupDocument::id.name, backupDocumentId),
            Updates.set(BackupDocument::rootDirectory.name, directory)
        )
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
}