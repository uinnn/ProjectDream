package dream.tiles

import dream.block.*
import dream.entity.*
import dream.entity.player.*
import dream.interfaces.*
import dream.level.*
import dream.level.chunk.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.packet.game.*
import dream.pos.*

/**
 * Represents a block tile entity.
 */
@Open
abstract class Tile(val type: TileType<out Tile>) : CompoundStorable, Keyable {
   
   /**
    * The pos of this tile
    */
   var pos = Pos.ZERO
   
   /**
    * The state of this tile.
    */
   var state = Blocks.AIR.state
   
   /**
    * The level that this tile is on.
    */
   lateinit var level: Level
   
   /**
    * Checks if this tile has a level.
    */
   val hasLevel get() = this::level.isInitialized
   
   /**
    * Gets the block metadata of this tile.
    */
   protected var cachedBlockMetadata = -1
   var blockMetadata: Int
      get() {
         if (cachedBlockMetadata == -1)
            cachedBlockMetadata = block.getMetadataFromState(state)
         return cachedBlockMetadata
      }
      set(value) {
         cachedBlockMetadata = value
      }
   
   /**
    * If this tile is invalid.
    */
   var isInvalid = false
   
   /**
    * Gets the key of this tile.
    */
   override val key get() = type.key
   
   /**
    * Gets the block of this tile.
    */
   val block get() = state.block
   
   /**
    * Gets the update packet for this tile.
    *
    * Update packets is used to send valid data of a tile to a client.
    */
   fun getUpdatePacket(): GamePacket? {
      return null
   }
   
   /**
    * Receive and trigger a client event.
    */
   fun triggerEvent(id: Int, value: Int): Boolean {
      return false
   }
   
   /**
    * Sets this tile as changed.
    */
   fun setChanged() {
      if (hasLevel) {
         state = level.getState(pos)
         cachedBlockMetadata = block.getMetadataFromState(state)
         if (!block.isAir) {
            level.updateComparators(pos, block)
         }
      }
   }
   
   fun resetTileInfo() {
      cachedBlockMetadata = -1
   }
   
   /**
    * Gets the distance between this tile to the given pos.
    */
   fun distance(other: Pos) = pos.distance(other)
   fun distance(other: Entity) = pos.distance(other)
   fun distance(x: Number, y: Number, z: Number) = pos.distance(x, y, z)
   
   /**
    * Checks if this tile is near to the given pos.
    */
   fun isNear(other: Pos, radius: Double) = pos.isNear(other, radius)
   fun isNear(other: Entity, radius: Double) = pos.isNear(other, radius)
   fun isNear(x: Number, y: Number, z: Number, radius: Double) = pos.isNear(x, y, z, radius)
   
   fun onInteract(player: Player, type: Click) {
   }
   
   /**
    * Called on [Chunk] when this tile is added to [chunk].
    */
   fun onAddedToChunk(chunk: Chunk) {
   
   }
   
   /**
    * Saves all additional data.
    */
   fun saveAdditional(tag: CompoundTag) {
   }
   
   override fun save(tag: CompoundTag) {
      tag["id"] = key.toString()
      tag["x"] = pos.flooredX
      tag["y"] = pos.flooredY
      tag["z"] = pos.flooredZ
      saveAdditional(tag)
   }
   
   override fun load(tag: CompoundTag) {
   }
}
