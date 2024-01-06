package dream.tiles

import dream.level.*

/**
 * A TileProvider interface represents an object capable of providing a tile
 * based on the specified level and metadata.
 */
interface TileProvider {
  
  /**
   * Provides a tile based on the specified level and metadata.
   *
   * @param level The level of the tile.
   * @param meta Additional metadata used to determine the tile.
   * @return The tile corresponding to the specified level and metadata.
   */
  fun provide(level: Level, meta: Int): Tile
}
