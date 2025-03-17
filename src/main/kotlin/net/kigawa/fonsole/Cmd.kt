package net.kigawa.fonsole

interface Cmd {
    suspend fun execute()
}
