package dream.tiles

import dream.collections.*
import dream.interfaces.*
import dream.item.*

/**
 * A chest tile.
 */
abstract class TileChest : TileContainer(Tiles.CHEST), Tickable {
   
   var items = ItemList(27)
   
   override val containerId: String get() = "minecraft:chest"
   
   override fun tick() {
   
   }
   
   override fun decrease(slot: Int, amount: Int): ItemStack {
      return items.decrease(slot, amount) { markDirty() }
   }
   
   
   
}
