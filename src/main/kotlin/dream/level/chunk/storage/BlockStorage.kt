package dream.level.chunk.storage

import dream.block.*
import dream.block.state.*
import dream.misc.*

/**
 * Represents a block storage.
 *
 * Each chunk section (chunks by 16x16x16) have a BlockStorage to holds information of blocks.
 */
@Open
class BlockStorage(var yBase: Int, storeSkylight: Boolean) {
   
   /**
    * A total count of the number of non-air blocks in this block storage.
    */
   var blockCount = 0
   
   /**
    * Contains the number of blocks in this block storage that requires ticking.
    *
    * Used to cull the Chunk from random tick updates for performance reasons.
    */
   var tickBlocksCount = 0
   
   /**
    * All stored data about the blocks.
    */
   var data = CharArray(4096)
   
   /**
    * The NibbleArray containing a block of Block-light data.
    */
   var blocklight = NibbleArray()
   
   /**
    * The NibbleArray containing a block of Sky-light data.
    */
   var skylight: NibbleArray? = if (storeSkylight) NibbleArray() else null
   
   /**
    * Checks if this block storage is empty.
    */
   val isEmpty get() = blockCount == 0
   
   /**
    * Checks if this block storage needs tick.
    */
   val needsTick get() = tickBlocksCount > 0
   
   /**
    * Gets the state id at the given coordinates.
    */
   fun getStateId(x: Int, y: Int, z: Int): Int {
      return data[y shl 8 or (z shl 4) or x].code
   }
   
   fun getStateOrNull(x: Int, y: Int, z: Int): IState? {
      return Blocks.stateByIdOrNull(getStateId(x, y, z))
   }
   
   /**
    * Gets the state at the given coordinates.
    */
   fun getState(x: Int, y: Int, z: Int, default: IState = Blocks.AIR.state): IState {
      return Blocks.stateById(getStateId(x, y, z), default)
   }
   
   /**
    * Sets the given [state] at the given coordinates.
    */
   operator fun set(x: Int, y: Int, z: Int, state: IState) {
      val oldState = getState(x, y, z)
      val oldBlock = oldState.block
      val newBlock = state.block
      
      if (!oldBlock.isAir) {
         --blockCount
         if (oldBlock.isTickable) {
            --tickBlocksCount
         }
      }
      
      if (!newBlock.isAir) {
         ++blockCount
         if (newBlock.isTickable) {
            ++tickBlocksCount
         }
      }
      
      data[getStateId(x, y, z)] = Blocks.STATES[state].toChar()
   }
   
   fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
      return Blocks.byStateIdOrNull(getStateId(x, y, z))
   }
   
   /**
    * Gets a block in the given coordinates.
    */
   fun getBlock(x: Int, y: Int, z: Int, default: Block = Blocks.AIR): Block {
      return Blocks.byStateId(getStateId(x, y, z), default)
   }
   
   /**
    * Gets a block metadata in the given coordinates.
    */
   fun getMetadata(x: Int, y: Int, z: Int): Int {
      val state = getState(x, y, z)
      return state.block.getMetadataFromState(state)
   }
   
   /**
    * Sets the saved Sky-light value in the block storage structure.
    */
   fun setSkylight(x: Int, y: Int, z: Int, value: Int) {
      skylight?.set(x, y, z, value)
   }
   
   /**
    * Gets the saved Sky-light value in the block storage structure.
    */
   fun getSkylight(x: Int, y: Int, z: Int): Int {
      return skylight?.get(x, y, z) ?: 0
   }
   
   /**
    * Sets the saved Block-light value in the block storage structure.
    */
   fun setBlocklight(x: Int, y: Int, z: Int, value: Int) {
      blocklight[x, y, z] = value
   }
   
   /**
    * Gets the saved Block-light value in the block storage structure.
    */
   fun getBlocklight(x: Int, y: Int, z: Int): Int {
      return blocklight[x, y, z]
   }
   
   /**
    * Recalculates all blocks from this storage.
    *
    * This get every block in a 16x16x16 area and updates [blockCount] and [tickBlocksCount] if neccessary.
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
   
}
