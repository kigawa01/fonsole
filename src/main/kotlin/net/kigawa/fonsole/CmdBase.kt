package net.kigawa.fonsole

abstract class CmdBase : Cmd {
    val config
        get() = Main.config
}