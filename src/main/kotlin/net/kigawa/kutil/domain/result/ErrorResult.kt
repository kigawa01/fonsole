package net.kigawa.kutil.domain.result

@Suppress("unused")
class ErrorResult<T, E>(val err: E) : Result<T, E>() {
    override fun getErrorOrNull(): E = err

    override fun getResultOrNull(): T? = null
}