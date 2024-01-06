package dream.tiles

import dream.api.Tickable

/**
 * A beacon tile.
 */
class TileBeacon : Tile(Tiles.BEACON), Tickable {
  override fun tick(partial: Int) {
  }
}
