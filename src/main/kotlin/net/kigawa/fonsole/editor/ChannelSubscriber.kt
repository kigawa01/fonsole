package net.kigawa.fonsole.editor

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelIterator
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.SelectClause1
import net.kigawa.fonsole.logger
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class ChannelSubscriber<T>(
    private val capacity: Int = 3,
) : Subscriber<T>, ReceiveChannel<T> {
    private val channel: Channel<T> = Channel(capacity = capacity)
    private var subscription: Subscription? = null
    private val logger = logger()
    override fun onSubscribe(s: Subscription?) {
        s!!.request(capacity.toLong())
        subscription = s
    }

    override fun onNext(t: T?) {
        runBlocking {
            channel.send(t!!)
        }
    }

    override fun onError(t: Throwable?) {
        logger.error(t?.message, t)
        channel.close()
    }

    override fun onComplete() {
        channel.close()
    }

    @DelicateCoroutinesApi
    override val isClosedForReceive: Boolean
        get() = channel.isClosedForReceive

    @ExperimentalCoroutinesApi
    override val isEmpty: Boolean
        get() = channel.isEmpty
    override val onReceive: SelectClause1<T>
        get() = channel.onReceive
    override val onReceiveCatching: SelectClause1<ChannelResult<T>>
        get() = channel.onReceiveCatching

    override suspend fun receive(): T {
        val value = channel.receive()
        subscription?.request(1)
        return value
    }

    override suspend fun receiveCatching(): ChannelResult<T> {
        val value = channel.receiveCatching()
        if (value.isSuccess) subscription?.request(1)
        return value
    }

    override fun tryReceive(): ChannelResult<T> {
        val value = channel.tryReceive()
        if (value.isSuccess) subscription?.request(1)
        return value
    }

    override fun iterator(): ChannelIterator<T> {
        val value = channel.iterator()
        return object : ChannelIterator<T> {
            override suspend fun hasNext(): Boolean {
                return value.hasNext()
            }

            override fun next(): T {
                val next = value.next()
                subscription?.request(1)
                return next
            }
        }
    }

    override fun cancel(cause: CancellationException?) {
        channel.cancel(cause)
    }

    @Deprecated("Deprecated in Java")
    override fun cancel(cause: Throwable?): Boolean {
        channel.cancel()
        return false
    }

    suspend fun join() {
        @Suppress("ControlFlowWithEmptyBody", "UseExpressionBody")
        for (a in this) {
        }
    }
}