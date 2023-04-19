package dream.block

import dream.block.material.*
import dream.block.state.*
import dream.item.*
import dream.level.*
import dream.pos.*
import dream.tab.*

class BlockBookshelf : Block(Materials.WOOD, CreativeTab.BLOCKS) {
   override fun getDrop(level: Level, pos: Pos, state: IState, fortune: Int): ItemStack {
      return stackOf(Items.BOOK, 3)
   }
}
