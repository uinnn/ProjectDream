package dream.errors

/**
 * Represents a minecraft exception error.
 */
class MinecraftException(message: String, cause: Throwable? = null) : Exception(message, cause) {

  /**
   * All categories information of this exception.
   */
  val categories = ArrayList<ExceptionCategory>(4)

  override fun printStackTrace() {
    println("An error occurred!\n")
    println("Categories information:")
    printCategories()
    println("\nStrack trace:")
    super.printStackTrace()
  }

  /**
   * Implements a category information inside this exception.
   */
  fun category(category: ExceptionCategory): MinecraftException {
    categories += category
    return this
  }

  /**
   * Implements a category information inside this exception.
   */
  fun category(category: String, reason: Any): MinecraftException {
    return category(ExceptionCategory(category, reason))
  }

  /**
   * Implements a category information inside this exception.
   */
  fun categories(vararg categories: Pair<String, Any>): MinecraftException {
    categories.forEach { category(it.first, it.second) }
    return this
  }

  /**
   * Prints all [categories]
   */
  fun printCategories() {
    for (category in categories) {
      category.print()
    }
  }
}

/**
 * A category for minecraft exception.
 */
data class ExceptionCategory(val category: String, val value: Any) {
  fun print() = println(toString())

  override fun toString(): String {
    return "$category: $value"
  }
}
