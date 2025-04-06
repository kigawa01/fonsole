package net.kigawa.fonsole.mongo

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.kigawa.fonsole.config.ConnectionConfig


class Client(
    connectionConfig: ConnectionConfig,
    client: MongoClient,
    syncClient: com.mongodb.reactivestreams.client.MongoClient,
) {
    val database = Database(
        client.getDatabase(connectionConfig.databaseName), syncClient.getDatabase(connectionConfig.databaseName),
        this
    )
    private val requests = Channel<suspend () -> Unit>(capacity = 3)

    init {
        repeat(connectionConfig.maxRequest) {
            CoroutineScope(Dispatchers.IO).launch {
                for (request in requests) {
                    request()
                }
            }
        }
    }

    companion object {
        suspend fun connect(connectionConfig: ConnectionConfig, block: suspend (Client) -> Unit) {
            MongoClients.create(connectionConfig.toUri().toString()).use { syncClient ->
                MongoClient.Factory.create(connectionConfig.toUri().toString()).use { client ->
                    block(Client(connectionConfig, client, syncClient))
                }
            }
        }
    }

    suspend fun <T> request(block: suspend () -> T): T {
        val deferred = CoroutineScope(Dispatchers.IO).async(start = CoroutineStart.LAZY) {
            block()
        }
        requests.send {
            deferred.start()
            deferred.join()
        }
        return deferred.await()
    }
}