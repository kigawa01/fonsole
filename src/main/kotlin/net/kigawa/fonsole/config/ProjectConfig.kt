package net.kigawa.fonsole.config

import java.nio.file.Path

data class ProjectConfig(
    val directory: Path,
    val projectName: String,
)