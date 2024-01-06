package dream.tiles

import dream.Key
import dream.api.Keyable
import dream.misc.Open

/**
 * Represents a tile entity type.
 */
@Open
data class TileType<T : Tile>(override val key: Key, val factory: TileSupplier<T>) : Keyable {
  fun create() = factory.create()
}
