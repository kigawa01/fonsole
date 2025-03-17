package net.kigawa.fonsole.model

data class Directory(
    val name: String,
    val files: List<FileModel>,
    val subDirectories: List<Directory>,
)
