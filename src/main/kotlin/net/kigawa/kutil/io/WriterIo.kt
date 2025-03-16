package net.kigawa.kutil.io

interface WriterIo<T> : Io {
  suspend fun writeLine(value: T)
}