package net.kigawa.kutil.validator.str

import net.kigawa.kutil.validator.Validator

class StrValidator<ORIGINAL : Any?, FROM : Any?>(parent: Validator<ORIGINAL, *, FROM>) :
  AbstractStrValidator<ORIGINAL, FROM>(parent) {

  override fun validateTask(from: FROM): String {
    return from.toString()
  }
}