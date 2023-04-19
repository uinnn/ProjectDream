package dream.errors

import kotlin.system.exitProcess

/**
 * Represents a crash error.
 */
class Crash(message: String, cause: Throwable? = null) : Error(message, cause) {
   
   /**
    * All categories information of this crash.
    */
   val categories = ArrayList<CrashCategory>(2)
   
   override fun printStackTrace() {
      println("An crash occurred!\n")
      println("Categories information:")
      printCategories()
      println("\nStrack trace:")
      super.printStackTrace()
   }
   
   /**
    * Implements a crash category information inside this crash.
    */
   fun category(category: CrashCategory): Crash {
      categories += category
      return this
   }
   
   /**
    * Implements a crash category information inside this crash.
    */
   fun category(category: String, reason: String): Crash {
      return category(CrashCategory(category, reason))
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
 * A category for crash.
 */
data class CrashCategory(val category: String, val reason: String) {
   fun print() = println(toString())
   
   override fun toString(): String {
      return """
            Category: $category
            Reason: $reason
        """.trimIndent()
   }
}

/**
 * Crashes the server.
 */
fun crash(): Nothing = throw Crash("The server crashed without arguments.")

/**
 * Catches any crash exceptions in [block].
 */
fun catchCrash(block: () -> Unit) {
   try {
      block()
   } catch (crash: Crash) {
      crash.printStackTrace()
      exitProcess(0)
   }
}
