package net.kigawa.fonsole

import ch.qos.logback.classic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.kigawa.fonsole.environment.EnvironmentConfig
import org.slf4j.LoggerFactory

object Main {
    val logger = LoggerFactory.getLogger(this::class.java)
    val config by lazy { EnvironmentConfig() }

    init {
        val root = LoggerFactory.getLogger("root") as Logger
        root.level = config.logLevel
        logger.info("log level: ${config.logLevel}")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val argList = args.toMutableList()
        while (argList.isNotEmpty()) {
            val first = argList.removeFirst()
            Tasks.entries.forEach {
                if (first == it.command) {
                    val job = CoroutineScope(Dispatchers.Default).launch {
                        it.execute()
                    }
                    runBlocking { job.join() }
                    return
                }
            }
        }
        logger.error("subcommand not found")
    }
}