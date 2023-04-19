package dream.library

import kotlinx.serialization.*

/**
 * Represents a library to be downloaded when server starts.
 */
@Serializable
data class Library(val group: String, val artifact: String, val version: String) {
   override fun toString() = "$group:$artifact:$version"
   
   companion object Factory {
      
      /**
       * Creates a new library data from [str].
       *
       * Examples:
       * ```
       * val lib = Library("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
       * ```
       *
       * @param delimiter the delimiter used to separate group/artifact/version
       */
      operator fun invoke(str: String, delimiter: Char = ':'): Library {
         val split = str.split(delimiter, limit = 3)
         return Library(split[0], split[1], split[2])
      }
   }
}
