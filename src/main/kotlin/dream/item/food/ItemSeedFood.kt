package dream.item.food

import dream.block.*
import dream.entity.player.*
import dream.item.*
import dream.item.misc.*
import dream.level.*
import dream.pos.*

/**
 * Represents a food item that's can be planted.
 */
class ItemSeedFood(food: Food, val crops: Block, val soil: Block) : ItemFood(food) {
   
   override fun onUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction, hit: Pos): Boolean {
      return ItemSeed.onSeedUse(level, item, player, pos, side)
   }
   
}
