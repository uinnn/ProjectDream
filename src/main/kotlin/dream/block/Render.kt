package dream.block

/**
 * Represents all type of renderization of a block.
 */
enum class Render(val id: Int) {
  NONE(-1),
  LIQUID(1),
  TILE(2),
  BLOCK(3);

  companion object {

    /**
     * Gets a render by [id].
     */
    fun byId(id: Int) = when (id) {
      -1 -> NONE
      1 -> LIQUID
      2 -> TILE
      else -> BLOCK
    }
  }
}
