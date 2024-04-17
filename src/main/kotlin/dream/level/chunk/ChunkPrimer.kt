package dream.level.chunk

import dream.block.*
import dream.block.state.*
import dream.misc.*

/**
 * This class represents a ChunkPrimer that stores block states for a chunk of the world.
 */
@Open
class ChunkPrimer {
  
  /**
   * An array that stores the data for block states.
   */
  val data = ShortArray(65536)
  
  /**
   * The default state for blocks (initialized to Blocks.AIR state).
   */
  val defaultState = Blocks.AIR.state
  
  /**
   * Gets the block state at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The block state at the specified coordinates.
   * @throws IndexOutOfBoundsException if the coordinates are out of range.
   */
  fun getState(x: Int, y: Int, z: Int): IState {
    return getState(x shl 12 or z shl 8 or y)
  }
  
  /**
   * Gets the block state at the specified index.
   *
   * @param index The index of the block state.
   * @return The block state at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  fun getState(index: Int): IState {
    val value = data.getOrNull(index) ?: throw IndexOutOfBoundsException("The coordinate is out of range")
    return Blocks.stateByIdOrNull(value.toInt()) ?: defaultState
  }
  
  /**
   * Sets the block state at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param state The block state to set.
   * @throws IndexOutOfBoundsException if the coordinates are out of range.
   */
  fun setState(x: Int, y: Int, z: Int, state: IState) {
    setState(x shl 12 or z shl 8 or y, state)
  }
  
  /**
   * Sets the block state at the specified index.
   *
   * @param index The index of the block state.
   * @param state The block state to set.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  fun setState(index: Int, state: IState) {
    if (index in data.indices) {
      data[index] = Blocks.STATES.getId(state).toShort()
    } else {
      throw IndexOutOfBoundsException("The coordinate is out of range")
    }
  }
}
