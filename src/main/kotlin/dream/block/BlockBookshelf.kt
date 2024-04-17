package dream.block

import dream.block.material.*
import dream.block.property.*
import dream.block.state.*
import dream.entity.player.*
import dream.item.*
import dream.item.misc.*
import dream.level.*
import dream.level.chunk.*
import dream.misc.*
import dream.pos.*
import dream.tab.*

class BlockBookshelf : Block(Materials.WOOD, CreativeTab.BLOCKS) {
  companion object {
    val BOOKS = PropertyInt("books", 1..2)
  }
  
  override fun getDrop(level: Level, pos: Pos, state: IState, fortune: Int, chance: Float): ItemStack {
    val books = state.getValue(BOOKS)
    return ItemStack(Items.BOOK, books)
  }
  
  override fun onRemoved(level: Level, pos: Pos, chunk: Chunk, state: IState) {
    getDrop(level, pos, state).drop(level, pos)
  }
  
  override fun onInteract(level: Level, pos: Pos, state: IState, player: Player, side: Direction, hit: Pos, click: Click): Boolean {
    if (click.isRight) {
      if (player.heldItem.its<ItemBook>()) {
        state.cycle(BOOKS)
        player.decreaseHeldItem()
      } else {
        player.addOrDrop(ItemStack(Items.BOOK), pos)
        state.with(BOOKS, state.getValue(BOOKS) - 1)
      }
    }
    return true
  }

  override fun createData(): BlockData {
    return RuntimeBlockData(this, BOOKS)
  }
}
