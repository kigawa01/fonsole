package net.kigawa.fonsole.editor

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.Logger

class ChannelReceiveSubscriber<T>(
    private val channel: Channel<T>,
    private val logger: Logger,
) : Subscriber<T> {
    override fun onSubscribe(s: Subscription?) {
        logger.debug("on subscribe")
        s!!.request(Long.MAX_VALUE)
    }

    override fun onNext(t: T?) {
        runBlocking {
            channel.send(t!!)
        }
    }

    override fun onError(t: Throwable?) {
        logger.error(t?.message, t)
    }

    override fun onComplete() {
        channel.close()
    }
}