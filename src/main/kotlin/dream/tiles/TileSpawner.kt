package dream.tiles

import dream.api.Tickable

/**
 * A spawner tile.
 */
class TileSpawner : Tile(Tiles.SPAWNER), Tickable {
  override fun tick(partial: Int) {
  }
}
