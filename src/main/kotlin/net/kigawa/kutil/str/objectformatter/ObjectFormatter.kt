package net.kigawa.kutil.str.objectformatter

interface ObjectFormatter {
  fun format(obj: Any?): String
}