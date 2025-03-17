package net.kigawa.fonsole.document

import net.kigawa.fonsole.model.DirectoryModel
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class BackupDocument(
    @BsonId
    val id: ObjectId,
    val startDate: LocalDateTime,
    val rootDirectory: DirectoryModel? = null,
    val endDate: LocalDateTime? = null,
)
