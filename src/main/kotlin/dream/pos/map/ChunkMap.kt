package dream.pos.map

/**
 * Represents a chunk-based map interface with generic type T.
 * @param T The type of values stored in the chunk.
 */
interface ChunkMap<T : Any> {

  /**
   * Gets the size of the chunk map.
   */
  val size: ChunkSize

  /**
   * Gets the underlying data array.
   */
  var data: ByteArray

  /**
   * Sets the data of this chunk map to the given array.
   */
  fun mutate(data: ByteArray)

  /**
   * Converts the given value to its corresponding identifier byte.
   * @param value The value to be converted.
   * @return The identifier byte for the value.
   */
  fun toId(value: T): Byte

  /**
   * Converts the given identifier byte to its corresponding value.
   * @return The value corresponding to the identifier.
   */
  fun toValue(id: Byte): T

  /**
   * Sets the value at the specified coordinates in the chunk map.
   */
  fun set(x: Int, y: Int, z: Int, value: Byte)

  /**
   * Sets the value at the specified coordinates in the chunk map.
   */
  fun set(x: Int, y: Int, z: Int, value: T)

  /**
   * Gets the identifier byte at the specified coordinates in the chunk map.
   * @return The identifier byte.
   */
  fun getId(x: Int, y: Int, z: Int): Byte

  /**
   * Removes the value at the specified coordinates in the chunk map.
   */
  fun remove(x: Int, y: Int, z: Int)

  /**
   * Checks if the specified coordinates are within the bounds of the chunk map.
   * @return True if coordinates are within the chunk map and has a value set, false otherwise.
   */
  fun contains(x: Int, y: Int, z: Int): Boolean

  /**
   * Gets the value at the specified coordinates in the chunk map.
   * @return The value at the specified coordinates.
   */
  fun getValue(x: Int, y: Int, z: Int): T

  /**
   * Gets the index of the specified coordinates in the chunk map.
   * @return The index of the coordinates.
   */
  fun indexOf(x: Int, y: Int, z: Int): Int

  /**
   * Checks if the specified coordinates are within the bounds of the chunk map.
   * @return True if coordinates are within the chunk map, false otherwise.
   */
  fun inChunk(x: Int, y: Int, z: Int): Boolean

  /**
   * Fills the entire chunk map with the specified byte value.
   * @param value The byte value to fill the chunk map with.
   */
  fun fill(value: Byte)

  /**
   * Fills the entire chunk map with the specified value.
   * @param value The value to fill the chunk map with.
   */
  fun fill(value: T)

  /**
   * Clears the chunk map, resetting all values to default.
   */
  fun clear()

  /**
   * Performs an action for each coordinate x within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtX(y: Int, z: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtX(y, z, block)
  }

  /**
   * Performs an action for each coordinate y within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtY(x: Int, z: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtY(x, z, block)
  }

  /**
   * Performs an action for each coordinate z within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtZ(x: Int, y: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtZ(x, y, block)
  }

  /**
   * Performs an action for each layer x within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtLayerX(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtLayerX(layer, block)
  }

  /**
   * Performs an action for each layer y within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtLayerY(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtLayerY(layer, block)
  }

  /**
   * Performs an action for each layer z within the chunk aside from the given coordinates.
   * @param block The action to be performed for each coordinate.
   */
  fun eachAtLayerZ(layer: Int, block: (x: Int, y: Int, z: Int) -> Unit) {
    size.eachAtLayerZ(layer, block)
  }

  /**
   * Performs an action for each coordinate (x, y, z) within the chunk.
   * @param block The action to be performed for each coordinate.
   */
  fun each(block: (x: Int, y: Int, z: Int) -> Unit) {
    size.each(block)
  }

  /**
   * Performs an action for each value at the specified X-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtX(y: Int, z: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtX(y, z) { x, _, _ -> block(x, y, z, getValue(x, y, z)) }
  }

  /**
   * Performs an action for each value at the specified Y-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtY(x: Int, z: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtY(x, z) { _, y, _ -> block(x, y, z, getValue(x, y, z)) }
  }

  /**
   * Performs an action for each value at the specified Z-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtZ(x: Int, y: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtZ(x, y) { _, _, z -> block(x, y, z, getValue(x, y, z)) }
  }

  /**
   * Performs an action for each value at the specified layer along the X-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtLayerX(layer: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtLayerX(layer) { x, _, _ -> block(x, layer, layer, getValue(x, layer, layer)) }
  }

  /**
   * Performs an action for each value at the specified layer along the Y-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtLayerY(layer: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtLayerY(layer) { _, y, _ -> block(layer, y, layer, getValue(layer, y, layer)) }
  }

  /**
   * Performs an action for each value at the specified layer along the Z-coordinate within the chunk.
   * @param block The action to be performed for each value.
   */
  fun eachValueAtLayerZ(layer: Int, block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    eachAtLayerZ(layer) { _, _, z -> block(layer, layer, z, getValue(layer, layer, z)) }
  }

  /**
   * Performs an action for each coordinate (x, y, z) within the chunk with the value in the coordinate.
   * @param block The action to be performed for each coordinate.
   */
  fun eachValue(block: (x: Int, y: Int, z: Int, value: T) -> Unit) {
    each { x, y, z -> block(x, y, z, getValue(x, y, z)) }
  }

}

