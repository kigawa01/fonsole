package net.kigawa.fonsole.backup

import net.kigawa.fonsole.CmdBase
import net.kigawa.fonsole.Main
import net.kigawa.fonsole.editor.BackupEditor
import net.kigawa.fonsole.editor.ProjectEditor
import net.kigawa.fonsole.model.Client
import net.kigawa.kutil.domain.result.SuccessResult

class BackupCmd : CmdBase() {
    override suspend fun execute() {
        Client.connect(config.connectionConfig, Main.logger) {
            val database = it.database
            val projectEditor = ProjectEditor(database, Main.logger, config.backupConfig)
            projectEditor.createProject()
            val backupEditor = BackupEditor(config.backupConfig, Main.logger, database)
            val backupDocument = backupEditor.createBackupDocument()
            if (backupDocument !is SuccessResult) return@connect
            backupEditor.insertBackupDocument(backupDocument.result)
        }
    }
}