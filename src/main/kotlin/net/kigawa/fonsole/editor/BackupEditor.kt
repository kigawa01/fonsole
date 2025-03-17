package net.kigawa.fonsole.editor

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import net.kigawa.fonsole.backup.BackupConfig
import net.kigawa.fonsole.document.BackupDocument
import net.kigawa.fonsole.model.Directory
import net.kigawa.fonsole.model.FileModel
import net.kigawa.kutil.domain.result.ErrorResult
import net.kigawa.kutil.domain.result.Result
import net.kigawa.kutil.domain.result.SuccessResult
import org.bson.types.ObjectId
import org.slf4j.Logger
import java.io.File

class BackupEditor(
    private val backupConfig: BackupConfig,
    private val logger: Logger,
    database: MongoDatabase,
) {
    private val collection = database.getCollection<BackupDocument>(BackupDocument::class.simpleName!!)

    suspend fun createBackupDocument(): Result<BackupDocument, Unit> {
        val dir = backupConfig.directory
        if (dir.isDirectory.not()) {
            logger.error("$dir is not directory")
            return ErrorResult(Unit)
        }

        return SuccessResult(
            BackupDocument(
                id = ObjectId(),
                rootDirectory = createDirectoryModel(dir)
            )
        )
    }

    private suspend fun createDirectoryModel(currentDirectory: File): Directory {
        return CoroutineScope(currentCoroutineContext()).async {
            Directory(
                name = currentDirectory.name,
                files = currentDirectory.listFiles { !it.isDirectory }.map { FileModel(it.name) },
                subDirectories = currentDirectory.listFiles { it.isDirectory }.map { createDirectoryModel(it) }
            )
        }.await()
    }

    suspend fun insertBackupDocument(backupDocument: BackupDocument) {
        collection.insertOne(backupDocument)
    }
}