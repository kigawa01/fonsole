package net.kigawa.fonsole.editor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import net.kigawa.fonsole.backup.BackupConfig
import net.kigawa.fonsole.document.ProjectDocument
import org.slf4j.Logger

class ProjectEditor(
    database: MongoDatabase,
    private val logger: Logger,
    private val backupConfig: BackupConfig,
) {
    private val projectCollection = database.getCollection<ProjectDocument>(ProjectDocument::class.simpleName!!)

    suspend fun createProject() {
        logger.info("setup unique index")
        val indexOptions = IndexOptions().unique(true)
        projectCollection.createIndex(
            Indexes.descending(ProjectDocument::name.name), indexOptions
        )
        logger.info("check project is exist")
        val documents = projectCollection.find(
            Filters.eq(ProjectDocument::name.name, backupConfig.projectName)
        )
        if (documents.firstOrNull() != null) return
        logger.info("create project")
        projectCollection.insertOne(
            ProjectDocument(
                name = backupConfig.projectName,
                backupIds = listOf()
            )
        )
    }

}