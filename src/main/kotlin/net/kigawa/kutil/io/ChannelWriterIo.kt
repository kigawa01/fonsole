package net.kigawa.kutil.io

import kotlinx.coroutines.channels.Channel

@Suppress("unused")
open class ChannelWriterIo<T>(
  protected val channel: Channel<T>,
) : WriterIo<T> {
  override suspend fun writeLine(value: T) {
    channel.send(value)
  }
}