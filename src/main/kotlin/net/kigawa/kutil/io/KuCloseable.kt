package net.kigawa.kutil.io

@OptIn(ExperimentalStdlibApi::class)
interface KuCloseable : AutoCloseable {
  override fun close()
}
