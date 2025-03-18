package net.kigawa.fonsole

import ch.qos.logback.classic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.kigawa.fonsole.config.EnvironmentConfig
import org.slf4j.LoggerFactory

object Main {
    val logger = LoggerFactory.getLogger(this::class.java)
    val config by lazy { EnvironmentConfig() }

    init {
        val root = LoggerFactory.getLogger("root") as Logger
        root.level = config.logLevel
        val mongo = LoggerFactory.getLogger("org.mongodb") as Logger
        mongo.level = config.mongoLogLevel
        logger.info("log level: ${config.logLevel}")
        logger.info("mongo log level: ${config.mongoLogLevel}")
    }

    @JvmStatic
    fun main(args: Array<String>) {
            val argList = args.toMutableList()
            while (argList.isNotEmpty()) {
                val first = argList.removeFirst()
                Cmds.entries.forEach { cmd ->
                    if (first == cmd.command) {
                        val job = CoroutineScope(Dispatchers.Default).launch {
                            cmd.execute()
                        }
                        runBlocking { job.join() }
                        return
                    }
                }
            }
            logger.error("subcommand not found")
        }
}