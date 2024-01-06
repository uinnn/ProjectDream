package dream.level

/**
 * Represents all dimension types of a world.
 */
enum class Dimension(val id: Int) {
  OVERWORLD(0),
  NETHER(-1),
  END(1);

  companion object {

    /**
     * Gets a dimension by id.
     */
    fun byId(id: Int) = when (id) {
      1 -> END
      -1 -> NETHER
      else -> OVERWORLD
    }
  }
}
