package dream.enchantment

import dream.item.*
import dream.item.tool.*

/**
 * Represents all targets for enchantments.
 */
enum class EnchantmentTarget {
   ALL,
   ARMOR,
   BOOTS,
   LEGGINGS,
   CHESTPLATE,
   HELMET,
   WEAPON,
   TOOL,
   RANGED_WEAPON,
   BOW,
   FISHING_ROD;
   
   /**
    * Determinates if [item] is appliable to be enchanted with this target.
    */
   fun isAppliable(item: Item): Boolean {
      return when (this) {
         ALL -> true
         ARMOR -> item is ItemArmor
         BOOTS -> item is ItemArmor && item.isBoots
         LEGGINGS -> item is ItemArmor && item.isLeggings
         CHESTPLATE -> item is ItemArmor && item.isChestplate
         HELMET -> item is ItemArmor && item.isHelmet
         WEAPON -> item is ItemWeapon
         TOOL -> item is ItemTool
         RANGED_WEAPON -> item is ItemRangedWeapon
         BOW -> item is ItemBow
         FISHING_ROD -> item is ItemFishingRod
      }
   }
}
