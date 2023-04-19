package dream.tiles

import dream.interfaces.*

/**
 * A enchantment table tile.
 */
abstract class TileEnchantmentTable : Tile(Tiles.ENCHANTMENT_TABLE),
   Tickable,
   Containerable {
   
   override val containerId: String get() = "minecraft:enchanting_table"
   
   override fun tick() {
   }
}
