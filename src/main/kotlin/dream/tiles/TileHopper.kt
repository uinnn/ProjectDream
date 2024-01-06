package dream.tiles

import dream.api.Tickable

/**
 * A hopper tile.
 */
abstract class TileHopper : TileContainer(Tiles.HOPPER), Hopper, Tickable {

  override val containerId: String get() = "minecraft:hopper"

  override fun tick(partial: Int) {

  }
}
