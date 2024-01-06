package dream.enchantment

import dream.*
import dream.entity.base.*
import dream.item.*
import dream.item.tool.*

/**
 * Represents sharpness enchantment.
 */
object Sharpness : EnchantmentDamage(16, minecraftKey("sharpness"), 5, EnchantmentTarget.WEAPON) {

  override val maxLevel get() = 5

  override fun getDamage(attacker: Entity, target: Entity, item: ItemStack, level: Int): Float {
    val tool = item.item
    return if (tool is ItemTool) {
      tool.damage + (level * 1.25f)
    } else {
      level.toFloat()
    }
  }

  override fun getMinEnchantability(level: Int): Int {
    return 1 + (level - 1) * 11
  }

  override fun getMaxEnchantability(level: Int): Int {
    return getMinEnchantability(level) + 20
  }

  override fun isAppliable(item: ItemStack): Boolean {
    return item.item is ItemAxe || super.isAppliable(item)
  }

  override fun isSupported(other: Enchantment): Boolean {
    return other !is Sharpness
  }
}
