package net.kigawa.kutil.diff
data class Diff<T>(val added: T, val removed: T)