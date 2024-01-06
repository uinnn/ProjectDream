package dream.item.misc

import dream.block.Block
import dream.entity.player.Player
import dream.item.*
import dream.level.Level
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
      return side == Direction.UP
    }
  }
}
