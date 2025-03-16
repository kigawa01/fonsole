package net.kigawa.kutil.validator

@Suppress("unused")
class EmptyValidator<T : Any?> : Validator<T, T, T> {
  override fun validate(value: T): T {
    return value
  }
}