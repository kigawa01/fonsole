package net.kigawa.kutil.io

interface ReaderIo<T> : Io {
  suspend fun read(): T
  suspend fun forEach(block: suspend (T) -> Unit)
}