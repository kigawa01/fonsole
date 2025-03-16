@file:Suppress("unused")

package net.kigawa.kutil.list

object KutilList {
  fun <T> connectList(vararg list: List<T>): List<T> {
    return list.flatMap {it}
  }
}
