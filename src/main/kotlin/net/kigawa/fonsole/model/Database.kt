package net.kigawa.fonsole.model

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets
import kotlin.reflect.KClass

class Database(
    private val database: MongoDatabase,
    private val syncDatabase: com.mongodb.reactivestreams.client.MongoDatabase,
) {
    fun <T : Any> getCollection(resultClass: KClass<T>): MongoCollection<T> =
        database.getCollection(resultClass.simpleName!!, resultClass.java)

    fun createBucket(name: String) = GridFSBuckets.create(syncDatabase, name)
}