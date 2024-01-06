package dream.level

/**
 * Represents all difficulty of a world.
 */
enum class Difficulty(val unlocalName: String) {
  PEACEFUL("options.difficulty.peaceful"),
  EASY("options.difficulty.easy"),
  NORMAL("options.difficulty.normal"),
  HARD("options.difficulty.hard");
  
  val id get() = ordinal

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
