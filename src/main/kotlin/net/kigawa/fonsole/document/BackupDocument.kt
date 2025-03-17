package net.kigawa.fonsole.document

import net.kigawa.fonsole.model.Directory
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class BackupDocument(
    @BsonId
    val id: ObjectId,
    val rootDirectory: Directory,
)
