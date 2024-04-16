package dream.level.chunk.storage

import dream.block.*
import dream.block.state.*
import dream.misc.*
import dream.nbt.CompoundStorable
import dream.nbt.types.CompoundTag
import dream.pos.*

/**
 * Represents a pallete.
 *
 * Each chunk section (chunks by 16x16x16) have a pallete to holds information of blocks.
 */
@Open
class Pallete(var yBase: Int, storeSkylight: Boolean) : CompoundStorable {
  companion object {
    const val SIZE = 4096
  }
  
  /**
   * A total count of the number of non-air blocks in this pallete.
   */
  var blockCount = 0
  
  /**
   * Contains the number of blocks in this pallete that requires ticking.
   *
   * Used to cull the Chunk from random tick updates for performance reasons.
   */
  var tickBlocksCount = 0
  
  /**
   * All stored data about the blocks.
   */
  var data = CharArray(SIZE)
  
  /**
   * The NibbleArray containing a block of Block-light data.
   */
  var blocklight = NibbleArray()
  
  /**
   * The NibbleArray containing a block of Sky-light data.
   */
  var skylight: NibbleArray? = if (storeSkylight) NibbleArray() else null
  
  /**
   * Checks if this pallete is empty.
   */
  val isEmpty get() = blockCount == 0
  
  /**
   * Checks if this pallete needs tick.
   */
  val needsTick get() = tickBlocksCount > 0
  
  /**
   * Retrieves the state ID of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The state ID of the block at the specified coordinates.
   */
  fun getStateId(x: Int, y: Int, z: Int): Int {
    return data[posToChunkIndex(x, y, z)].code
  }
  
  /**
   * Retrieves the state of the block at the specified coordinates, or null if no block state is found.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The state of the block at the specified coordinates, or null if no block state is found.
   */
  fun getStateOrNull(x: Int, y: Int, z: Int): IState? {
    return Blocks.stateByIdOrNull(getStateId(x, y, z))
  }
  
  /**
   * Retrieves the state of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param default The default state to return if no block state is found. Default value is [Blocks.AIR.state].
   * @return The state of the block at the specified coordinates, or the default state if no block state is found.
   */
  fun getState(x: Int, y: Int, z: Int, default: IState = Blocks.AIR.state): IState {
    return Blocks.stateById(getStateId(x, y, z), default)
  }
  
  /**
   * Retrieves the block at the specified coordinates, or null if no block is found.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The block at the specified coordinates, or null if no block is found.
   */
  fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
    return Blocks.byStateIdOrNull(getStateId(x, y, z))
  }
  
  /**
   * Retrieves the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param default The default block to return if no block is found. Default value is [Blocks.AIR].
   * @return The block at the specified coordinates, or the default block if no block is found.
   */
  fun getBlock(x: Int, y: Int, z: Int, default: Block = Blocks.AIR): Block {
    return Blocks.byStateId(getStateId(x, y, z), default)
  }
  
  /**
   * Sets the state of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param state The new state to set for the block.
   */
  operator fun set(x: Int, y: Int, z: Int, state: IState) {
    val oldState = getState(x, y, z)
    
    if (!oldState.isAir) {
      --blockCount
      if (oldState.isTickable) {
        --tickBlocksCount
      }
    }
    
    if (!state.isAir) {
      ++blockCount
      if (state.isTickable) {
        ++tickBlocksCount
      }
    }
    
    data[posToChunkIndex(x, y, z)] = state.id.toChar()
  }
  
  /**
   * Retrieves the metadata of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The metadata of the block at the specified coordinates.
   */
  fun getMetadata(x: Int, y: Int, z: Int): Int {
    return getState(x, y, z).getMetadata()
  }
  
  /**
   * Sets the skylight value of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param value The skylight value to set for the block.
   */
  fun setSkylight(x: Int, y: Int, z: Int, value: Int) {
    skylight?.set(x, y, z, value)
  }
  
  /**
   * Retrieves the skylight value of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The skylight value of the block at the specified coordinates, or 0 if no skylight data is available.
   */
  fun getSkylight(x: Int, y: Int, z: Int): Int {
    return skylight?.get(x, y, z) ?: 0
  }
  
  /**
   * Sets the blocklight value of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @param value The blocklight value to set for the block.
   */
  fun setBlocklight(x: Int, y: Int, z: Int, value: Int) {
    blocklight[x, y, z] = value
  }
  
  /**
   * Retrieves the blocklight value of the block at the specified coordinates.
   *
   * @param x The x-coordinate of the block.
   * @param y The y-coordinate of the block.
   * @param z The z-coordinate of the block.
   * @return The blocklight value of the block at the specified coordinates.
   */
  fun getBlocklight(x: Int, y: Int, z: Int): Int {
    return blocklight[x, y, z]
  }
  
  /**
   * Recalculates the total count of blocks and tickable blocks in the data structure.
   */
  fun recalculateBlocks() {
    blockCount = 0
    tickBlocksCount = 0
    for (x in 0 until 16) {
      for (y in 0 until 16) {
        for (z in 0 until 16) {
          val block = getBlock(x, y, z)
          if (!block.isAir) {
            ++this.blockCount
            if (block.isTickable) {
              ++this.tickBlocksCount
            }
          }
        }
      }
    }
  }
  
  /**
   * Fills the entire pallete with the specified state.
   *
   * This function sets every block within the pallete to the provided state.
   *
   * @param state The state to fill the pallete with.
   */
  fun fill(state: IState) {
    // Reset block count and tickable block count.
    blockCount = 0
    tickBlocksCount = 0
    
    // fill the pallete with the provided state
    data.fill(state.id.toChar())

    // Update block counts based on the provided state.
    if (!state.isAir) {
      blockCount = SIZE
      if (state.isTickable) {
        tickBlocksCount = SIZE
      }
    }
  }
  
  /**
   * Clears the entire pallete by filling it with air blocks.
   */
  fun clear() {
    blockCount = 0
    tickBlocksCount = 0
    data.fill(Blocks.AIR.id.toChar())
  }

  override fun save(tag: CompoundTag) {
    tag["Y"] = (yBase shr 4 and 255).toByte()
    val blocks = ByteArray(data.size)
    val blocksArray = NibbleArray()
    var extraBlocksArray: NibbleArray? = null
    for ((index, id) in data.withIndex()) {
      val code = id.code
      val x = index and 15
      val y = index shr 8 and 15
      val z = index shr 4 and 15
      if (code shr 12 != 0) {
        if (extraBlocksArray == null) {
          extraBlocksArray = NibbleArray()
        }
        extraBlocksArray[x, y, z] = code shr 12
      }
      blocks[index] = (code shr 4 and 255).toByte()
      blocksArray[x, y, z] = code and 15
    }
    tag["Blocks"] = blocks
    tag["Data"] = blocksArray.data
    if (extraBlocksArray != null) {
      tag["Add"] = extraBlocksArray.data
    }
    tag["BlockLight"] = blocklight.data
    tag["SkyLight"] = skylight!!.data
  }

  override fun load(tag: CompoundTag) {
    yBase = tag.byte("Y").toInt() shl 4
    val blocks = tag.byteArray("Blocks")
    val blocksArray = NibbleArray(tag.byteArray("Data"))
    val extraBlocksArray: NibbleArray? = if (tag.has("Add")) NibbleArray(tag.byteArray("Add")) else null
    val data = CharArray(blocks.size)
    for (index in data.indices) {
      val x = index and 15
      val y = index shr 8 and 15
      val z = index shr 4 and 15
      val extra = extraBlocksArray?.get(x, y, z) ?: 0
      val block = (blocks[index].toInt() and 255) shl 4
      data[index] = (extra shl 12 or block or blocksArray[x, y, z]).toChar()
    }
    this.data = data
    blocklight = NibbleArray(tag.byteArray("BlockLight"))
    skylight = NibbleArray(tag.byteArray("SkyLight"))
  }
}
