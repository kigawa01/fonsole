package net.kigawa.kutil.concurrent

import kotlinx.coroutines.CoroutineScope

open class CoroutineException(message: String?, coroutineContext: CoroutineScope, cause: Throwable?) :
  RuntimeException("$message | context: $coroutineContext", cause) {
}