package dream.tiles

/**
 * A supplier for creating tile entities.
 */
fun interface TileSupplier<T : Tile> {
  fun create(): T
}
