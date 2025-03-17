package net.kigawa.fonsole.model

data class DirectoryModel(
    val name: String,
    val files: List<FileModel>,
    val subDirectories: List<DirectoryModel>,
)
