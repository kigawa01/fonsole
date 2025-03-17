package net.kigawa.fonsole.config

import java.net.URI

data class ConnectionConfig(
    val username: String,
    val password: String,
    val host: String,
    val port: Int,
    val databaseName: String,
) {
    fun toUri(): URI = URI("mongodb://$username:$password@$host:$port")
}