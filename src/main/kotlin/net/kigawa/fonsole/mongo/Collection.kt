package net.kigawa.fonsole.mongo

import com.mongodb.kotlin.client.coroutine.MongoCollection

class Collection<T : Any>(
    private val database: Database,
    private val collection: MongoCollection<T>,
) {

    suspend fun <U> request(block: suspend MongoCollection<T>.() -> U): U =
        database.request { collection.block() }
}