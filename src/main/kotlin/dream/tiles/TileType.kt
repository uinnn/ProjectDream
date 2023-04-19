package dream.tiles

import dream.*
import dream.interfaces.*
import dream.misc.*

/**
 * Represents a tile entity type.
 */
@Open
data class TileType<T : Tile>(override val key: Key, val factory: TileSupplier<T>) : Keyable {
   fun create() = factory.create()
}
