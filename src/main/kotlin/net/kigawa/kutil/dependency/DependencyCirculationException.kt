package net.kigawa.kutil.dependency

class DependencyCirculationException: RuntimeException {
  constructor(message: String, cause: Throwable?, vararg dependencies: Any?): super(
    message + " " + dependencies.toList(),
    cause
  )
  
  constructor(message: String, vararg dependencies: Any?): this(message, null, dependencies = dependencies)
}