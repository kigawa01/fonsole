package net.kigawa.fonsole.environment

import io.github.cdimascio.dotenv.dotenv
import net.kigawa.fonsole.mongo.ConnectionConfig

class EnvironmentConfig {
    val dotenv by lazy {
        dotenv {
            ignoreIfMissing = true
        }.entries() + dotenv {
            ignoreIfMissing = true
            filename = ".env.local"
        }.entries()
    }
    val connectionConfig by lazy {
        ConnectionConfig(
            username = readString("MONGO_USERNAME"),
            password = readString("MONGO_PASSWORD"),
            host = readString("MONGO_HOST", "localhost"),
            port = readInt("MONGO_PORT", 27017),
            databaseName = readString("MONGO_DATABASE_NAME", "fonsole")
        )
    }

    private fun readEnv(key: String) = dotenv.firstOrNull { it.key == key }?.value

    private fun readString(key: String, defaultValue: String? = null): String =
        readEnv(key) ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")


    private fun readInt(key: String, defaultValue: Int? = null): Int =
        readEnv(key)?.toInt() ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")

}