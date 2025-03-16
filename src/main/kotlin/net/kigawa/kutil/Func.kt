package net.kigawa.kutil

import kotlinx.coroutines.channels.Channel


suspend inline fun <T> Channel<T>.forEach(func: (T) -> Unit) {
  for (item in this) {
    func(item)
  }
}
