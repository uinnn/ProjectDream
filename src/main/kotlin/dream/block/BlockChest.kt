package dream.block

import dream.api.*
import dream.level.*
import dream.pos.*
import dream.tiles.*

class BlockChest : BlockTile() {
  override fun provide(level: Level, meta: Int): Tile {
    TODO()
  }
  
  fun getLockableContainer(level: Level, pos: Pos): LockableContainer {
    TODO()
  }
}
