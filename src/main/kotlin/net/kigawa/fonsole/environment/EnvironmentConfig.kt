package net.kigawa.fonsole.environment

import ch.qos.logback.classic.Level
import io.github.cdimascio.dotenv.dotenv
import net.kigawa.fonsole.config.ConnectionConfig
import net.kigawa.fonsole.config.ProjectConfig
import net.kigawa.fonsole.config.RestoreConfig
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EnvironmentConfig {
    companion object {
        private val datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

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

    val projectConfig by lazy {
        ProjectConfig(
            directory = readPath("BACKUP_PATH"),
            projectName = readString("PROJECT_NAME"),
        )
    }
    val restoreConfig by lazy {
        RestoreConfig(
            restoreDate = readDate("RESTORE_DATE")
        )
    }

    private fun readEnv(key: String) = dotenv.firstOrNull { it.key == key }?.value

    private fun readString(key: String, defaultValue: String? = null): String =
        readEnv(key) ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")


    private fun readInt(key: String, defaultValue: Int? = null): Int =
        readEnv(key)?.toInt() ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")

    private fun readPath(key: String, defaultValue: Path? = null): Path =
        readEnv(key)?.let { Path.of("", it) } ?: defaultValue ?: throw IllegalArgumentException("$key is not defined")

    private fun readDate(key: String, defaultValue: LocalDateTime? = null): LocalDateTime =
        readEnv(key)?.let { LocalDateTime.parse(it, datetimeFormatter) } ?: defaultValue
        ?: throw IllegalArgumentException(
            "$key is not defined"
        )

}