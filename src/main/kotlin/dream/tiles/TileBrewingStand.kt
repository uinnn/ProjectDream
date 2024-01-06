package dream.tiles

import dream.api.Tickable

/**
 * A brewing stand tile.
 */
class TileBrewingStand : Tile(Tiles.BREWING_STAND), Tickable {
  override fun tick(partial: Int) {
  }
}
