package net.kigawa.kutil.domain.result

sealed class Result<T, E> {
    abstract fun getErrorOrNull(): E?
    abstract fun getResultOrNull(): T?
}