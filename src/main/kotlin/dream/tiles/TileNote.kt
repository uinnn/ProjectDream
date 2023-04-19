package dream.tiles

import dream.block.state.*
import dream.nbt.types.*
import dream.pos.*
import dream.utils.*

/**
 * A note tile.
 */
class TileNote : Tile(Tiles.NOTE) {
   var note: Byte = 0
   var previousRedstone = false
   
   fun changePitch() {
      note = ((note + 1) % 25).toByte()
      setChanged()
   }
   
   fun triggerNote() {
   
   }
   
   override fun saveAdditional(tag: CompoundTag) {
      super.saveAdditional(tag)
      tag["note"] = note
   }
   
   override fun load(tag: CompoundTag) {
      super.load(tag)
      note = tag.byte("note").clamp(0, 24)
   }
}
