package net.kigawa.fonsole.editor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import net.kigawa.fonsole.config.ProjectConfig
import net.kigawa.fonsole.document.ProjectDocument
import net.kigawa.fonsole.mongo.Database
import net.kigawa.kutil.domain.result.ErrorResult
import net.kigawa.kutil.domain.result.Result
import net.kigawa.kutil.domain.result.SuccessResult
import org.bson.types.ObjectId
import org.slf4j.Logger

class ProjectEditor(
    database: Database,
    private val logger: Logger,
    private val projectConfig: ProjectConfig,
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
            Filters.eq(ProjectDocument::name.name, projectConfig.projectName)
        )
        if (documents.firstOrNull() != null) return
        logger.info("create project")
        collection.insertOne(
            ProjectDocument(
                name = projectConfig.projectName,
                backupIds = listOf()
            )
        )
    }

    suspend fun addBackupId(backupDocumentId: ObjectId) {
        collection.findOneAndUpdate(
            Filters.eq(ProjectDocument::name.name, projectConfig.projectName),
            Updates.push(ProjectDocument::backupIds.name, backupDocumentId)
        )
    }

    suspend fun findBackups(): Result<List<ObjectId>, Unit> {
        val document = collection.find(Filters.eq(ProjectDocument::name.name, projectConfig.projectName)).singleOrNull()
        if (document == null) {
            logger.error("project is not single")
            return ErrorResult(Unit)
        }
        return SuccessResult(document.backupIds)
    }
}