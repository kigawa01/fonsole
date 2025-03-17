package net.kigawa.fonsole.backup

import java.io.File

data class BackupConfig(
    val directory: File,
    val projectName: String,
)
