package dream.enchantment

import dream.*
import dream.entity.*
import dream.item.*
import dream.item.tool.*

/**
 * Represents a enchantment that can perform damage on a entity.
 */
class EnchantmentDamage(
   id: Int,
   key: Key,
   weight: Int,
   target: EnchantmentTarget,
) : Enchantment(id, key, weight, target) {
   
   /**
    * Gets the damage amount for this enchantment.
    */
   fun getDamage(attacker: Entity, target: Entity, item: ItemStack, level: Int): Float {
      val tool = item.item
      return if (tool is ItemTool) {
         tool.damage + level
      } else {
         level.toFloat()
      }
   }
   
   /**
    * Damages [target] by [attacker] by this enchantment.
    */
   fun damage(attacker: Entity, target: Entity, item: ItemStack, level: Int): Boolean {
      return target.onEnchantmentDamage(this, attacker, item, level)
   }
}
