package dream.tiles

import dream.api.Tickable

/**
 * A ender chest tile.
 */
class TileEnderChest : Tile(Tiles.ENDER_CHEST), Tickable {
  override fun tick(partial: Int) {
  }
}
