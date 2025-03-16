package net.kigawa.fonsole

abstract class TaskBase : Task {
    val config
        get() = Main.config
}