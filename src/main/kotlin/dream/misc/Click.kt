package dream.misc

import dream.interfaces.*

/**
 * Represents two types of mouse click.
 */
public enum class Click(override val id: String) : Id {
   LEFT("left"),
   RIGHT("right");
   
   val isLeft get() = this == LEFT
   val isRight get() = this == RIGHT
   
   companion object {
      
      /**
       * Gets a click by id.
       */
      public fun byId(id: String): Click {
         return byIdOrNull(id) ?: error("unknown click type of id '$id'")
      }
      
      /**
       * Gets a click by id or null.
       */
      public fun byIdOrNull(id: String): Click? {
         return when (id.lowercase()) {
            "left" -> LEFT
            "right" -> RIGHT
            else -> null
         }
      }
   }
}
