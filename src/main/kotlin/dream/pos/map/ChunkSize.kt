package dream.pos.map

import kotlin.math.abs

/**
 * Represents a chunk of 3D space defined by minimum and maximum coordinates in each dimension.
 * @property minX The minimum X-coordinate.
 * @property minY The minimum Y-coordinate.
 * @property minZ The minimum Z-coordinate.
 * @property maxX The maximum X-coordinate.
 * @property maxY The maximum Y-coordinate.
 * @property maxZ The maximum Z-coordinate.
 */
data class ChunkSize(val minX: Int, val minY: Int, val minZ: Int, val maxX: Int, val maxY: Int, val maxZ: Int) {
  companion object {
    @JvmField val EMPTY = ChunkSize(0, 0, 0, 0, 0, 0)
  }

  /**
   * Gets the size of the chunk in the X dimension.
   */
  val sizeX get() = maxX - minX

  /**
   * Gets the size of the chunk in the Y dimension.
   */
  val sizeY get() = maxY - minY

  /**
   * Gets the size of the chunk in the Z dimension.
   */
  val sizeZ get() = maxZ - minZ

  /**
   * Gets the volume of the chunk.
   */
  val volume get() = abs(sizeX * sizeY * sizeZ)

  /**
   * Checks if the given coordinates (x, y, z) are within the chunk.
   */
  fun contains(x: Int, y: Int, z: Int): Boolean {
    return x in minX..maxX && y in minY..maxY && z in minZ..maxZ
  }

  /**
   * Gets the checked index for the given coordinates (x, y, z) within the chunk.
   */
  fun checkedIndex(x: Int, y: Int, z: Int): Int {
    return ((x - minX) * sizeY * sizeZ + (y - minY) * sizeZ + (z - minZ)) - 1
  }

  /**
   * Gets the index for the given coordinates (x, y, z) within the chunk,
   * or -1 if the coordinates are outside the chunk.
   */
  fun indexOf(x: Int, y: Int, z: Int): Int {
    if (!contains(x, y, z)) return -1
    return checkedIndex(x, y, z)
  }

  /**
   * Performs an action for each coordinate x within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtX(y: Int, z: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (x in minX..maxX) {
      block(x, y, z)
    }
  }

  /**
   * Performs an action for each coordinate y within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtY(x: Int, z: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (y in minY..maxY) {
      block(x, y, z)
    }
  }

  /**
   * Performs an action for each coordinate z within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtZ(x: Int, y: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (z in minZ..maxZ) {
      block(x, y, z)
    }
  }

  /**
   * Performs an action for each layer x within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtLayerX(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (y in minY..maxY) {
      for (z in minZ..maxZ) {
        block(layer, y, z)
      }
    }
  }

  /**
   * Performs an action for each layer y within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtLayerY(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (x in minX..maxX) {
      for (z in minZ..maxZ) {
        block(x, layer, z)
      }
    }
  }

  /**
   * Performs an action for each layer z within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  inline fun eachAtLayerZ(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    for (x in minX..maxX) {
      for (y in minY..maxY) {
        block(x, y, layer)
      }
    }
  }

  /**
   * Performs an action for each coordinate (x, y, z) within the chunk.
   * @param block The action to be performed for each coordinate.
   */
  inline fun each(block: (x: Int, y: Int, z: Int) -> Unit) {
    for (x in minX..maxX) {
      for (y in minY..maxY) {
        for (z in minZ..maxZ) {
          block(x, y, z)
        }
      }
    }
  }
}
