package dream.level.accessor

import dream.block.*
import dream.block.property.*
import dream.block.state.*
import dream.pos.*

/**
 * Represents a acessor for blocks
 */
interface BlockAccessor {
   
   fun getStateOrNull(x: Int, y: Int, z: Int): IState?
   fun getStateOrNull(pos: Pos): IState? = getStateOrNull(pos.flooredX, pos.flooredY, pos.flooredZ)
   
   /**
    * Gets a block state for the given pos.
    */
   fun getState(x: Int, y: Int, z: Int, default: IState = Blocks.AIR.state): IState
   
   fun getBlockOrNull(x: Int, y: Int, z: Int): Block?
   fun getBlockOrNull(pos: Pos): Block? = getBlockOrNull(pos.flooredX, pos.flooredY, pos.flooredZ)
   
   /**
    * Gets a block for the given pos.
    */
   fun getBlock(x: Int, y: Int, z: Int, default: Block = Blocks.AIR): Block
   
   /**
    * Gets a block state for the given pos.
    */
   fun getState(pos: Pos, default: IState = Blocks.AIR.state): IState {
      return getState(pos.flooredX, pos.flooredY, pos.flooredZ, default)
   }
   
   /**
    * Gets a block for the given pos.
    */
   fun getBlock(pos: Pos, default: Block = Blocks.AIR): Block {
      return getBlock(pos.flooredX, pos.flooredY, pos.flooredZ, default)
   }
   
   /**
    * Sets the block state at given pos.
    */
   fun setState(x: Int, y: Int, z: Int, state: IState, flags: Int = 3): Boolean
   
   /**
    * Sets the block state at given pos.
    */
   fun setState(pos: Pos, state: IState, flags: Int = 3): Boolean {
      return setState(pos.flooredX, pos.flooredY, pos.flooredZ, state, flags)
   }
   
   /**
    * Sets the block at given pos.
    *
    * The state will be the default block state.
    */
   fun setBlock(x: Int, y: Int, z: Int, block: Block, flags: Int = 3): Boolean {
      return setState(x, y, z, block.state, flags)
   }
   
   /**
    * Sets the block at given pos.
    *
    * The state will be the default block state.
    */
   fun setBlock(pos: Pos, block: Block, flags: Int = 3): Boolean {
      return setState(pos, block.state, flags)
   }
   
   /**
    * Sets the block state at given pos with the given [property] value [value].
    */
   fun <T : Any> withState(x: Int, y: Int, z: Int, property: Property<T>, value: T): Boolean {
      val state = getState(x, y, z).with(property, value)
      return setState(x, y, z, state)
   }
   
   /**
    * Sets the block state at given pos with the given [property] value [value].
    */
   fun <T : Any> withState(pos: Pos, property: Property<T>, value: T): Boolean {
      return withState(pos.flooredX, pos.flooredY, pos.flooredZ, property, value)
   }
   
   /**
    * Cycles the block state at given pos by [property].
    */
   fun cycleState(x: Int, y: Int, z: Int, property: Prop): Boolean {
      val state = getState(x, y, z).cycle(property)
      return setState(x, y, z, state)
   }
   
   /**
    * Cycles the block state at given pos by [property].
    */
   fun cycleState(pos: Pos, property: Prop): Boolean {
      return cycleState(pos.flooredX, pos.flooredY, pos.flooredZ, property)
   }
}
