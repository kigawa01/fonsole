package net.kigawa.kutil.list


@Suppress("unused")
fun <E> Collection<E>.containsIf(filter: (E) -> Boolean): Boolean {
  forEach {
    if (filter(it)) return true
  }
  return false
}

@Suppress("unused")
fun <E, R : Any> Collection<E>.firstOrNullMap(filter: (E) -> R?): R? {
  forEach {
    return filter.invoke(it) ?: return@forEach
  }
  return null
}
