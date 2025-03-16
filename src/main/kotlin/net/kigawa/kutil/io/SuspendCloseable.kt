package net.kigawa.kutil.io

interface SuspendCloseable {
  suspend fun suspendClose()
}