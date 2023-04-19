package dream.tiles

import dream.nbt.types.*

/**
 * A comparator tile.
 */
class TileComparator : Tile(Tiles.COMPARATOR) {
   var signal = 0
   
   override fun saveAdditional(tag: CompoundTag) {
      tag["Signal"] = signal
   }
   
   override fun load(tag: CompoundTag) {
      signal = tag.int("Signal")
   }
}
