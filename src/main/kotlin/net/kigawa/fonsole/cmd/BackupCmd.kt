package net.kigawa.fonsole.cmd

import net.kigawa.fonsole.Main
import net.kigawa.fonsole.editor.BackupEditor
import net.kigawa.fonsole.editor.ProjectEditor
import net.kigawa.fonsole.model.Client
import org.bson.types.ObjectId

class BackupCmd : CmdBase() {
    override suspend fun execute() {
        Client.Companion.connect(config.connectionConfig, Main.logger) {
            val database = it.database
            val projectEditor = ProjectEditor(database, Main.logger, config.projectConfig)
            projectEditor.createProject()
            val backupEditor = BackupEditor(config.projectConfig, Main.logger, database)

            val backupDocumentId = ObjectId()
            projectEditor.addBackupId(backupDocumentId)
            backupEditor.insertBackupDocument(backupDocumentId)

            backupEditor.uploadBackup(backupDocumentId)
        }
    }
}