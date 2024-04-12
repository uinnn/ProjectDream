package dream.storage

/**
 * Abstract base class for implementing the ChunkMap interface with generic type T.
 * @param T The type of values stored in the chunk map.
 * @param size The size of the chunk map.
 */
abstract class BaseChunkMap<T : Any>(override val size: ChunkSize) : ChunkMap<T> {

  /**
   * The underlying data array to store values in the chunk map.
   */
  override val data = ByteArray(size.volume) { -1 }

  /**
   * Sets the byte value at the specified coordinates in the chunk map.
   * @param value The byte value to be set.
   */
  override fun set(x: Int, y: Int, z: Int, value: Byte) {
    val index = indexOf(x, y, z)
    if (index >= 0) {
      data[index] = value
    }
  }

  /**
   * Sets the value at the specified coordinates in the chunk map using the identifier byte.
   * @param value The value to be set.
   */
  override fun set(x: Int, y: Int, z: Int, value: T) = set(x, y, z, toId(value))

  /**
   * Gets the identifier byte at the specified coordinates in the chunk map.
   * @return The identifier byte.
   */
  override fun getId(x: Int, y: Int, z: Int): Byte {
    val index = indexOf(x, y, z)
    return if (index < 0) -1 else data[index]
  }

  /**
   * Removes the value at the specified coordinates in the chunk map.
   */
  override fun remove(x: Int, y: Int, z: Int) {
    val index = indexOf(x, y, z)
    if (index >= 0) {
      data[index] = -1
    }
  }

  /**
   * Checks if the specified coordinates are within the bounds of the chunk map and contain a valid value.
   * @return True if coordinates are within the chunk map and contain a valid value, false otherwise.
   */
  override fun contains(x: Int, y: Int, z: Int): Boolean {
    val index = indexOf(x, y, z)
    return index >= 0 && data[index].toInt() != -1
  }

  /**
   * Gets the value at the specified coordinates in the chunk map.
   * @return The value at the specified coordinates.
   */
  override fun getValue(x: Int, y: Int, z: Int): T = toValue(getId(x, y, z))

  /**
   * Gets the index of the specified coordinates in the chunk map.
   * @return The index of the coordinates.
   */
  override fun indexOf(x: Int, y: Int, z: Int): Int = size.indexOf(x, y, z)

  /**
   * Checks if the specified coordinates are within the bounds of the chunk map.
   * @return True if coordinates are within the chunk map, false otherwise.
   */
  override fun inChunk(x: Int, y: Int, z: Int): Boolean = size.contains(x, y, z)

  /**
   * Fills the entire chunk map with the specified byte value.
   * @param value The byte value to fill the chunk map with.
   */
  override fun fill(value: Byte) = data.fill(value)

  /**
   * Fills the entire chunk map with the specified value.
   * @param value The value to fill the chunk map with.
   */
  override fun fill(value: T) = fill(toId(value))

  /**
   * Clears the chunk map, resetting all values to the default.
   */
  override fun clear() = fill(-1)
}
