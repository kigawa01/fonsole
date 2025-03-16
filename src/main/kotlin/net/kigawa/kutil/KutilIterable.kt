@file:Suppress("unused")

package net.kigawa.kutil

/**
 * utilities about array
 */
object KutilIterable {
  /**
   * test obj contain in array
   *
   * @param iterable array to test
   * @param obj      obj to test
   * @param <S>      source class type
   * @param <T>      test class type
   * @return return true when contain
  </T></S> */
  fun <T> contain(iterable: Iterable<T>, obj: T): Boolean {
    return iterable.contains(obj)
  }
  
  fun <T> toList(iterable: Iterable<T>): List<T> {
    return iterable.toList()
  }
  
  /**
   * creat set from array
   *
   * @param ts  base array
   * @param <T> class type
   * @return created set
  </T> */
  fun <T> toSet(ts: Array<T>): Set<T> {
    return ts.toSet()
  }
}
