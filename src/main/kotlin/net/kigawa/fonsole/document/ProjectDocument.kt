package net.kigawa.fonsole.document

import org.bson.types.ObjectId

data class ProjectDocument(
    val name: String,
    val backupIds: List<ObjectId>,
)
