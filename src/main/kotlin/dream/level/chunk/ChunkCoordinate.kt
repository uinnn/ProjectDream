package dream.level.chunk

import dream.pos.*

/**
 * Represents a chunk coordinate pair.
 */
data class ChunkCoordinate(val x: Int, val z: Int) {

  /**
   * The center X of this chunk.
   */
  val centerX get() = startX + 8

  /**
   * The center Z of this chunk.
   */
  val centerZ get() = startZ + 8

  /**
   * Get the first world X coordinate that belongs to this Chunk.
   */
  val startX get() = x shl 4

  /**
   * Get the first world Z coordinate that belongs to this Chunk.
   */
  val startZ get() = z shl 4

  /**
   * Get the last world X coordinate that belongs to this Chunk.
   */
  val endX get() = startX + 15

  /**
   * Get the last world Z coordinate that belongs to this Chunk.
   */
  val endZ get() = startZ + 15

  /**
   * Get the World coordinates of the Block with the given Chunk coordinates relative to this chunk.
   */
  fun getPos(x: Int, y: Int, z: Int): Pos {
    return Pos(startX + x, y, startZ + z)
  }

  /**
   * Get the coordinates of the Block in the center of this chunk with the given Y coordinate.
   */
  fun getCenterPos(y: Int): Pos {
    return Pos(centerX, y, centerZ)
  }

  /**
   * Converts this chunk coordinates to long.
   */
  fun toLong(): Long {
    return chunkCoordinateToLong(x, z)
  }

  override fun toString(): String {
    return "[$x, $z]"
  }

  companion object {

    /**
     * Converts the chunk coordinates to long.
     */
    fun chunkCoordinateToLong(x: Int, z: Int): Long {
      return x.toLong() and 4294967295L or (z.toLong() and 4294967295L shl 32)
    }
  }
}
