package net.kigawa.kutil.io

@Suppress("unused")
class FunctionWriterIo<T>(
  private val func: (T)->Unit
): WriterIo<T> {
  override suspend fun writeLine(value: T) {
    func(value)
  }
}