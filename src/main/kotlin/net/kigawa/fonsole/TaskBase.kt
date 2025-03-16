package net.kigawa.fonsole

import net.kigawa.fonsole.environment.EnvironmentConfig

abstract class TaskBase : Task {
    val config by lazy { EnvironmentConfig() }
}