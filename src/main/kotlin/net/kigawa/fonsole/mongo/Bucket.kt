package net.kigawa.fonsole.mongo

import com.mongodb.reactivestreams.client.gridfs.GridFSBucket

class Bucket(
    private val database: Database,
    private val bucket: GridFSBucket,
) {
    suspend fun <T>request(block: suspend GridFSBucket.() -> T) = database.request { bucket.block() }
}