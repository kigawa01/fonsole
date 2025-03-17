package net.kigawa.fonsole.model

import kotlin.time.Duration

data class BackupRequirements(
    val period: Duration?,
    val interval: Duration,
)
