package net.kigawa.kutil.domain.result

@Suppress("unused")
class SuccessResult<T, E>(
    val result: T,
) : Result<T, E>() {
    override fun getErrorOrNull(): E? = null

    override fun getResultOrNull(): T = result
}