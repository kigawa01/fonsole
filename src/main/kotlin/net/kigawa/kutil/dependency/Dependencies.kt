package net.kigawa.kutil.dependency

@Suppress("unused")
class DependencyStack<T>(
  private val stack: List<T>,
) {
  constructor() : this(mutableListOf())

  fun add(dependency: T): DependencyStack<T> {
    if (stack.contains(dependency)) throw DependencyCirculationException("unit has bean circular reference", dependency)
    val list = stack.toMutableList()
    list.add(dependency)
    return DependencyStack(stack)
  }
}