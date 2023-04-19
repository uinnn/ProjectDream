package dream.errors

/**
 * Represents a crash error.
 */
class MinecraftException(message: String, cause: Throwable? = null) : Exception(message, cause) {

   /**
    * All categories information of this crash.
    */
   val categories = ArrayList<ExceptionCategory>(2)

   override fun printStackTrace() {
      println("An error occurred!\n")
      println("Categories information:")
      printCategories()
      println("\nStrack trace:")
      super.printStackTrace()
   }

   /**
    * Implements a crash category information inside this crash.
    */
   fun category(category: ExceptionCategory): MinecraftException {
      categories += category
      return this
   }

   /**
    * Implements a crash category information inside this crash.
    */
   fun category(category: String, reason: Any): MinecraftException {
      return category(ExceptionCategory(category, reason))
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
