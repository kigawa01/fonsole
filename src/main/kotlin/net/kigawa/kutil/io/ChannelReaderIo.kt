package net.kigawa.kutil.io

import kotlinx.coroutines.channels.Channel
import net.kigawa.kutil.forEach

@Suppress("unused")
open class ChannelReaderIo<T>(
  protected val channel: Channel<T>,
) : ReaderIo<T> {

  override suspend fun read(): T {
    return channel.receive()
  }

  override suspend fun forEach(block: suspend (T) -> Unit) {
    channel.forEach { block(it) }
  }

}