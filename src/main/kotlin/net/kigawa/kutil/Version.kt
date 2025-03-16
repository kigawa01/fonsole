package net.kigawa.kutil

@Suppress("unused")
class Version(
  private val strVersion: String,
) {
  fun isAfter(before: Version): Boolean {
    val ownIntVer = intVersion()
    before.intVersion().forEachIndexed { index, value ->
      val own = ownIntVer.getOrNull(index) ?: return false
      if (own == value) return@forEachIndexed
      return own > value
    }
    return false
  }

  fun intVersion(): List<Int> {
    val result = mutableListOf<Int>()
    var section = ""
    strVersion.forEach {
      if (it.code in 48..57) {
        section += it
        return@forEach
      }
      if (section == "") return@forEach
      result.add(section.toInt())
      section = ""
    }
    if (section != "") result.add(section.toInt())
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Version

    return strVersion == other.strVersion
  }

  override fun hashCode(): Int {
    return strVersion.hashCode()
  }
}
