package net.kigawa.kutil.validator.str

import net.kigawa.kutil.validator.AbstractValidator
import net.kigawa.kutil.validator.Validator

abstract class AbstractStrValidator<ORIGINAL : Any?, FROM : Any?>(parent: Validator<ORIGINAL, *, FROM>) :
  AbstractValidator<ORIGINAL, FROM, String>(parent) {
}