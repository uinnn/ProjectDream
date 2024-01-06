package dream.extension

/**
 * Represents a plugin module.
 *
 * Plugin module is used to specify some entries to identify and
 * analyze for informational purposes' plugin data.
 */
@Target(AnnotationTarget.CLASS)
annotation class Module(
  val name: String,
  val version: String = "1.0",
  val author: String = "",
  val colaborators: Array<String> = [],
)
