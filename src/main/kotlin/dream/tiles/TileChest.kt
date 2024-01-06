package dream.tiles

import dream.api.*
import dream.collections.*
import dream.item.*

/**
 * A chest tile.
 */
abstract class TileChest : TileContainer(Tiles.CHEST), Tickable {

  override var items = ItemList(27)

  override val containerId: String get() = "minecraft:chest"

  override fun tick(partial: Int) {

  }

  override fun decrease(slot: Int, amount: Int): ItemStack {
    return items.decrease(slot, amount) { markDirty() }
  }


}
