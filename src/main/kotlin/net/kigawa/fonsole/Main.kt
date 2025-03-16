package net.kigawa.fonsole

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val argList = args.toMutableList()
        while (argList.isNotEmpty()) {
            val first = argList.removeFirst()
            Tasks.entries.forEach {
                if (first == it.command) {
                    it.execute()
                    return
                }
            }
        }
    }
}