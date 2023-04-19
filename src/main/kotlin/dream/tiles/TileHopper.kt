package dream.tiles

import dream.interfaces.*

/**
 * A hopper tile.
 */
abstract class TileHopper : TileContainer(Tiles.HOPPER), Hopper, Tickable {
   
   override val containerId: String get() = "minecraft:hopper"
   
   override fun tick() {
   
   }
}
