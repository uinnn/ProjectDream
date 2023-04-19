package dream.enchantment

import dream.*
import dream.interfaces.*
import dream.item.*
import dream.misc.*

/**
 * Represents a abstract enchantment.
 */
@Open
abstract class Enchantment(
   val id: Int,
   override val key: Key,
   val weight: Int,
   val target: EnchantmentTarget,
) : Keyable, Nameable {
   
   /**
    * Gets the name of this enchantment.
    */
   override val name get() = key.key
   
   /**
    * Gets the minimal level possible for this enchantment.
    */
   val minLevel get() = 1
   
   /**
    * Gets the maximal level possible for this enchantment.
    */
   val maxLevel get() = 1
   
   /**
    * Gets the minimal value of enchantability needed.
    */
   fun getMinEnchantability(level: Int): Int {
      return 1 + level * 10
   }
   
   /**
    * Gets the maximal value of enchantability needed.
    */
   fun getMaxEnchantability(level: Int): Int {
      return getMinEnchantability(level) * 5
   }
   
   /**
    * Determinates if [item] is appliable to be enchanted with this enchantment.
    */
   fun isAppliable(item: ItemStack): Boolean {
      return target.isAppliable(item.item)
   }
   
   /**
    * Determinates if this enchantment is supported together on a item with [other].
    */
   fun isSupported(other: Enchantment): Boolean {
      return other != this
   }
}
