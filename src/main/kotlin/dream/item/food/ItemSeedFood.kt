package dream.item.food

import dream.block.Block
import dream.entity.player.Player
import dream.item.ItemStack
import dream.item.misc.ItemSeed
import dream.level.Level
import dream.pos.*

/**
 * Represents a food item that's can be planted.
 */
class ItemSeedFood(food: Food, val crops: Block, val soil: Block) : ItemFood(food) {

  override fun onUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction, hit: Pos): Boolean {
    return ItemSeed.onSeedUse(level, item, player, pos, side)
  }

}
