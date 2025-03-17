package net.kigawa.fonsole

import net.kigawa.fonsole.backup.BackupCmd

enum class Tasks(
    val command: String,
    val newTask: () -> Cmd,
) {
    Backup("backup", ::BackupCmd),
    ;

    suspend fun execute() = newTask().execute()
}