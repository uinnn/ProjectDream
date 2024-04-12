package dream.block

import dream.block.material.Materials
import dream.block.property.PropertyInt
import dream.block.state.IState
import dream.entity.player.Player
import dream.item.ItemStack
import dream.item.Items
import dream.item.misc.ItemBook
import dream.level.Level
import dream.level.chunk.Chunk
import dream.misc.Click
import dream.pos.Direction
import dream.pos.Pos
import dream.tab.CreativeTab

class BlockBookshelf : Block(Materials.WOOD, CreativeTab.BLOCKS) {
  companion object {
    val BOOKS = PropertyInt("books", 0..4)
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
}
