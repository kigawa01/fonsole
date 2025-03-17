package net.kigawa.fonsole.editor

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import net.kigawa.fonsole.backup.BackupConfig
import net.kigawa.fonsole.document.ProjectDocument
import net.kigawa.fonsole.model.Database
import org.bson.types.ObjectId
import org.slf4j.Logger

class ProjectEditor(
    database: Database,
    private val logger: Logger,
    private val backupConfig: BackupConfig,
) {
    private val collection = database.getCollection(ProjectDocument::class)

    suspend fun createProject() {
        logger.info("setup unique index")
        val indexOptions = IndexOptions().unique(true)
        collection.createIndex(
            Indexes.descending(ProjectDocument::name.name), indexOptions
        )
        logger.info("check project is exist")
        val documents = collection.find(
            eq(ProjectDocument::name.name, backupConfig.projectName)
        )
        if (documents.firstOrNull() != null) return
        logger.info("create project")
        collection.insertOne(
            ProjectDocument(
                name = backupConfig.projectName,
                backupIds = listOf()
            )
        )
    }

    suspend fun addBackupId(backupDocumentId: ObjectId) {
        collection.findOneAndUpdate(
            eq(ProjectDocument::name.name, backupConfig.projectName),
            Updates.push(ProjectDocument::backupIds.name, backupDocumentId)
        )
    }
}