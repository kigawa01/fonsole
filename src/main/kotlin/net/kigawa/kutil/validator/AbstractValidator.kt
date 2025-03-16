package net.kigawa.kutil.validator

import net.kigawa.kutil.validator.str.StrValidator

abstract class AbstractValidator<ORIGINAL : Any?, FROM : Any?, TO : Any?>(
  private val parent: Validator<ORIGINAL, *, FROM>
) : Validator<ORIGINAL, FROM, TO> {
  final override fun validate(value: ORIGINAL): TO {
    val from = parent.validate(value)
    return validateTask(from)
  }

  abstract fun validateTask(from: FROM): TO

  fun str(): StrValidator<ORIGINAL, TO> {
    return StrValidator(this)
  }
}