package dream.tiles

import dream.api.Tickable

/**
 * A piston tile.
 */
class TilePiston : Tile(Tiles.PISTON), Tickable {
  override fun tick(partial: Int) {
  }
}
