package net.kigawa.fonsole.model

import com.mongodb.kotlin.client.coroutine.ClientSession
import com.mongodb.kotlin.client.coroutine.MongoClient
import net.kigawa.fonsole.mongo.ConnectionConfig
import org.slf4j.Logger

class Client(
    connectionConfig: ConnectionConfig,
    private val client: MongoClient,
    private val logger: Logger,
) {
    val database = client.getDatabase(connectionConfig.databaseName)

    companion object {
        suspend fun connect(connectionConfig: ConnectionConfig, logger: Logger, block: suspend (Client) -> Unit) {
            MongoClient.create(connectionConfig.toUri().toString()).use {
                block(Client(connectionConfig, it, logger))
            }
        }
    }

    suspend fun transaction(block: suspend (ClientSession) -> Unit) {
        val session = client.startSession()
        try {
            session.startTransaction()
            block(session)
            session.commitTransaction()
        } catch (e: Throwable) {
            session.abortTransaction()
            if (e is TransactionAbortException) return
            logger.error("transaction error", e)
            throw e
        }
    }
}