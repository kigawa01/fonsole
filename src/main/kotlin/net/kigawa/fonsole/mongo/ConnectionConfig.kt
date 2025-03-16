package net.kigawa.fonsole.mongo

import java.net.URI

data class ConnectionConfig(
    val username: String,
    val password: String,
    val host: String,
    val port: Int,
    val databaseName: String,
) {
    fun toUri(): URI = URI("mongodb+srv://$username:$password@$host:$port")
}
