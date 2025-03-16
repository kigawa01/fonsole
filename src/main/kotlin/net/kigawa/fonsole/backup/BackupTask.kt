package net.kigawa.fonsole.backup

import net.kigawa.fonsole.TaskBase
import net.kigawa.fonsole.mongo.Database

class BackupTask : TaskBase() {
    override fun execute() {
        Database.connect(config.connectionConfig) {

        }
    }
}