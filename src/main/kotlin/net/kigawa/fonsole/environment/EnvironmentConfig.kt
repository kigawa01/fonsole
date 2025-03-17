package net.kigawa.fonsole.environment

import ch.qos.logback.classic.Level
import io.github.cdimascio.dotenv.dotenv
import net.kigawa.fonsole.backup.BackupConfig
import net.kigawa.fonsole.mongo.ConnectionConfig
import java.io.File

class EnvironmentConfig {
    val dotenv by lazy {
        dotenv {
            ignoreIfMissing = true
            filename = ".env.local"
        }.entries() + dotenv {
            ignoreIfMissing = true
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
    val logLevel by lazy { Level.toLevel(readString("LOG_LEVEL", "INFO"), Level.INFO) }
    val mongoLogLevel by lazy { Level.toLevel(readString("MONGO_LOG_LEVEL", "INFO"), Level.INFO) }

    val backupConfig by lazy {
        BackupConfig(
            directory = readFile("BACKUP_PATH"),
            projectName = readString("PROJECT_NAME"),
        )
    }

    private fun readEnv(key: String) = dotenv.firstOrNull { it.key == key }?.value

    private fun readString(key: String, defaultValue: String? = null): String =
        readEnv(key) ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")


    private fun readInt(key: String, defaultValue: Int? = null): Int =
        readEnv(key)?.toInt() ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")

    private fun readFile(key: String, defaultValue: File? = null): File =
        readEnv(key)?.let { File(it) } ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")

}