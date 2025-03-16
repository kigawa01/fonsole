package net.kigawa.kutil.process

import net.kigawa.kutil.io.ReaderIo
import net.kigawa.kutil.io.SuspendCloseable
import net.kigawa.kutil.io.WriterIo

@Suppress("unused")
interface KuProcess : SuspendCloseable {
  fun reader(): ReaderIo<String>
  fun errReader(): ReaderIo<String>
  fun writer(): WriterIo<String>
  suspend fun waitFor()
}