package net.kigawa.fonsole

import net.kigawa.fonsole.backup.BackupTask

enum class Tasks(
    val command: String,
    val newTask: () -> Task,
) {
    Backup("backup", ::BackupTask),
    ;

    fun execute() = newTask().execute()
}