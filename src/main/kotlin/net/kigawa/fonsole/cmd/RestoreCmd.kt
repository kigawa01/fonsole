package net.kigawa.fonsole.cmd

import net.kigawa.fonsole.Main
import net.kigawa.fonsole.editor.BackupEditor
import net.kigawa.fonsole.editor.ProjectEditor
import net.kigawa.fonsole.mongo.Client
import net.kigawa.kutil.domain.result.SuccessResult

class RestoreCmd : CmdBase() {
    override suspend fun execute() {
        Client.Companion.connect(config.connectionConfig) {
            val database = it.database
            val projectEditor = ProjectEditor(database, Main.logger, config.projectConfig)
            val backups = projectEditor.findBackups()
            if (backups !is SuccessResult) return@connect

            val backupEditor = BackupEditor(config.projectConfig, Main.logger, database)
            val backup = backupEditor.findBackup(config.restoreConfig.restoreDate, backups.result)
            if (backup !is SuccessResult) return@connect
            Main.logger.info("restore backup: ${backup.result.id}")
            backupEditor.downloadBackup(backup.result)
        }
    }
}