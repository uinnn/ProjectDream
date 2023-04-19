package dream.tiles

import dream.level.*

interface TileProvider {
   fun createNew(level: Level, meta: Int): Tile
}
