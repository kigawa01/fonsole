package net.kigawa.kutil.validator

interface Validator<ORIGINAL : Any?, FROM : Any?, TO : Any?> {
  fun validate(value: ORIGINAL): TO

}