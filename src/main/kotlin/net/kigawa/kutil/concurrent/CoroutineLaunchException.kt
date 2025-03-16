package net.kigawa.kutil.concurrent

import kotlinx.coroutines.CoroutineScope

@Suppress("unused")
class CoroutineLaunchException(message: String?, coroutineContext: CoroutineScope, cause: Throwable?) :
  CoroutineException(message, coroutineContext, cause) {
}