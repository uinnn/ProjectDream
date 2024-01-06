package dream.item

import dream.enchantment.Sharpness
import dream.entity.player.Player
import dream.level.Level
import dream.pos.Direction
import dream.pos.Pos

class ItemEnergizer : Item() {
  override fun onUse(level: Level, item: ItemStack, player: Player, pos: Pos, side: Direction, hit: Pos): Boolean {
    pos.running(side.opposite(), item.tag.int("energy")) { relative ->
      val state = level.getState(relative)
      state.getDrop(level, relative, item.level(Sharpness)).drop(level, relative)
      level.setAir(relative)
    }

    return true
  }
}
