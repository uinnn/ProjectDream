package dream.storage

/**
 * Abstract class representing a chunk map with a shared size.
 * @param T The type of values stored in the chunk.
 */
abstract class SharedSizeChunkMap<T : Any> : BaseChunkMap<T>(ChunkSize.EMPTY) {

  /**
   * Gets the shared size of the chunk map.
   * @return The shared size of the chunk map.
   */
  override val size get() = size()

  /**
   * Gets the shared size of the chunk map.
   * @return The shared size of the chunk map.
   */
  abstract fun size(): ChunkSize

  /**
   * Overrides the base data array with a new array based on the shared size.
   */
  override val data = ByteArray(size.volume) { -1 }
}

