package dream.block

import dream.block.material.*
import dream.block.state.*
import dream.level.*
import dream.level.chunk.*
import dream.pos.*
import dream.tab.*
import dream.tiles.*

abstract class BlockTile : Block, TileProvider {
  
  constructor() : super(Materials.AIR)
  constructor(material: Material) : super(material)
  constructor(material: Material, tab: CreativeTab) : super(material, tab)
  
  init {
    isTileEntity = true
  }
  
  override val render: Render
    get() = Render.NONE
  
  override fun onRemoved(level: Level, pos: Pos, chunk: Chunk, state: IState) {
    super.onRemoved(level, pos, chunk, state)
    level.removeTile(pos)
  }
  
  override fun onEventReceived(level: Level, pos: Pos, state: IState, id: Int, value: Int): Boolean {
    super.onEventReceived(level, pos, state, id, value)
    val tile = level.getTile(pos)
    return tile != null && tile.triggerEvent(id, value)
  }
  
  fun isInvalidNeighbor(level: Level, pos: Pos, side: Direction, state: IState): Boolean {
    return state.material == Materials.CACTUS
  }
  
  fun isInvalidNeighbor(level: Level, pos: Pos, side: Direction): Boolean {
    return isInvalidNeighbor(level, pos, side, level.getState(pos.relative(side)))
  }
  
  fun hasInvalidNeighbor(level: Level, pos: Pos): Boolean {
    return Direction.horizontal.any { isInvalidNeighbor(level, pos, it) }
  }
  
}
