package net.kigawa.kutil.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class KutilIo {
}


@Suppress("unused")
inline fun <T> Channel<T>.dispatchForEach(coroutineContext: CoroutineContext, crossinline func: (T) -> Unit) {
  CoroutineScope(coroutineContext).launch {
    for (item in this@dispatchForEach) {
      func(item)
    }
  }
}