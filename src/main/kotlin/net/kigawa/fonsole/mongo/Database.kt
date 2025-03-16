package net.kigawa.fonsole.mongo

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class Database private constructor(
    val database: MongoDatabase,
) {

    companion object {
        fun connect(connectionConfig: ConnectionConfig, block: (Database) -> Unit) {
            MongoClient.create(connectionConfig.toUri().toString()).use {
                val database = it.getDatabase(connectionConfig.databaseName)
                block(Database(database))
            }
        }
    }

    inline fun <reified T : Any> getCollection(name: String) {
        val collection = database.getCollection<T>(name)
//        runBlocking {
//            val doc = collection.find(eq("title", "Back to the Future")).firstOrNull()
//            if (doc != null) {
//                println(doc)
//            } else {
//                println("No matching documents found.")
//            }
//        }
//        mongoClient.close()
    }
}