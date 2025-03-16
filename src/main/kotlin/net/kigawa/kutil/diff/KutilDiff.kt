@file:Suppress("unused")

package net.kigawa.kutil.diff

object KutilDiff {

  fun <T> getIterableDiff(oldIterable: Iterable<T>, newIterable: Iterable<T>): Diff<List<T>> {
    return Diff(
      newIterable.filter {
        !oldIterable.contains(it)
      },
      oldIterable.filter {
        !newIterable.contains(it)
      }
    )
  }
}