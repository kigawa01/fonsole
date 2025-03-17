package net.kigawa.fonsole.mongo

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets
import kotlin.reflect.KClass

class Database(
    private val database: MongoDatabase,
    private val syncDatabase: com.mongodb.reactivestreams.client.MongoDatabase,
    private val client: Client,
) {
    fun <T : Any> getCollection(resultClass: KClass<T>) =
        Collection(this, database.getCollection(resultClass.simpleName!!, resultClass.java))

    fun createBucket(name: String) = Bucket(this,GridFSBuckets.create(syncDatabase, name))
    suspend fun <T> request(block:suspend () -> T): T =
        client.request(block)

}