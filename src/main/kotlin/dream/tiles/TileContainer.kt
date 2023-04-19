package dream.tiles

import dream.entity.player.*
import dream.interfaces.*
import dream.misc.*
import dream.nbt.types.*

/**
 * A base tile for containers.
 */
abstract class TileContainer(type: TileType<out Tile>) : Tile(type), LockableContainer {
   
   override var code = LockCode("")
   override val isLocked get() = !code.isEmpty
   
   fun canOpen(player: Player): Boolean {
      return code.unlocks(player.heldItem)
   }
   
   override fun saveAdditional(tag: CompoundTag) {
      code.save(tag)
   }
   
   override fun load(tag: CompoundTag) {
      code.load(tag)
   }
}
