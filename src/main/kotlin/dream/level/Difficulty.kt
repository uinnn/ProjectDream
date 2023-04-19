package dream.level

/**
 * Represents all difficulty of a world.
 */
enum class Difficulty(val id: Int, val unlocalName: String) {
   PEACEFUL(0, "options.difficulty.peaceful"),
   EASY(1, "options.difficulty.easy"),
   NORMAL(2, "options.difficulty.normal"),
   HARD(3, "options.difficulty.hard");
   
   companion object {
      
      /**
       * Gets a difficulty by id.
       */
      fun byId(id: Int) = when (id) {
         0 -> PEACEFUL
         1 -> EASY
         3 -> HARD
         else -> NORMAL
      }
   }
}
