package dream.item.misc

import dream.block.*
import dream.entity.player.*
import dream.item.*
import dream.level.*
import dream.pos.*

/**
 * Represents a seed item.
 */
class ItemSeed(val crops: Block, val soil: Block) : Item() {
   
   override fun onUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction, hit: Pos): Boolean {
      return onSeedUse(level, item, player, pos, side)
   }
   
   companion object {
      
      /**
       * Do logic processor when using a seed.
       */
      fun onSeedUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction): Boolean {
         if (side != Direction.UP)
            return false
         
         return true
      }
   }
}
