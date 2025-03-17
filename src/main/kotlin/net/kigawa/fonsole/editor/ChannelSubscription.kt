package net.kigawa.fonsole.editor

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscription

class ChannelSubscription(
    val channel: Channel<Long>,
) : Subscription {
    override fun request(n: Long) {
        runBlocking { channel.send(n) }
    }

    override fun cancel() {
        channel.close()
    }
}