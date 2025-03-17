package net.kigawa.fonsole.model

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import net.kigawa.fonsole.mongo.ConnectionConfig
import org.slf4j.Logger

class Client(
    connectionConfig: ConnectionConfig,
    client: MongoClient,
    private val logger: Logger,
    syncClient: com.mongodb.reactivestreams.client.MongoClient,
) {
    val database = Database(
        client.getDatabase(connectionConfig.databaseName), syncClient.getDatabase(connectionConfig.databaseName)
    )

    companion object {
        suspend fun connect(connectionConfig: ConnectionConfig, logger: Logger, block: suspend (Client) -> Unit) {
            MongoClients.create(connectionConfig.toUri().toString()).use { syncClient ->
                MongoClient.create(connectionConfig.toUri().toString()).use { client ->
                    block(Client(connectionConfig, client, logger, syncClient))
                }
            }
        }
    }

}