package net.kigawa.fonsole.editor

import kotlinx.coroutines.channels.Channel
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel

class FileReadPublisher(
    private val byteChannel: ByteChannel,
) : Publisher<ByteBuffer> {
    val request = Channel<Long>(capacity = 3)
    lateinit var subscriber: Subscriber<in ByteBuffer>
    override fun subscribe(s: Subscriber<in ByteBuffer>?) {
        this.subscriber = s!!
        s.onSubscribe(ChannelSubscription(request))
    }

    suspend fun write() {
        for (request in request) {
            repeat(request.toInt()) {
                val buffer = ByteBuffer.allocate(1024)
                val cnt = byteChannel.read(buffer)
                if (cnt < 0) {
                    subscriber.onComplete()
                    return
                }
                subscriber.onNext(buffer)
            }
        }
    }
}