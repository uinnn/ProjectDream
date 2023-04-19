package dream.item.tool

import dream.item.*
import dream.misc.*

/**
 * Represents a material data for armor items.
 */
@Open
class ArmorMaterial(
   var durabilityFactor: Int,
   var helmetDefense: Int,
   var chestplateDefense: Int,
   var leggingsDefense: Int,
   var bootsDefense: Int,
   var enchantability: Int,
   var hasColor: Boolean,
   var repairItem: Item,
) {
   
   /**
    * Gets the durability of this armor material.
    */
   fun getDurability(type: ArmorType): Int {
      return type.durability * durabilityFactor
   }
   
   /**
    * Gets the defense based on [type].
    */
   fun getDefense(type: ArmorType): Int {
      return when (type) {
         ArmorType.HELMET -> helmetDefense
         ArmorType.CHESTPLATE -> chestplateDefense
         ArmorType.LEGGINGS -> leggingsDefense
         ArmorType.BOOTS -> bootsDefense
      }
   }
   
   override fun toString(): String {
      return "ArmorMaterial(durabilityFactor=$durabilityFactor, helmetDefense=$helmetDefense, chestplateDefense=$chestplateDefense, leggingsDefense=$leggingsDefense, bootsDefense=$bootsDefense, enchantability=$enchantability, repairItem=$repairItem)"
   }
   
   companion object {
      @JvmField val LEATHER = ArmorMaterial(5, 1, 3, 2, 1, 15, true, Items.AIR)
      @JvmField val CHAIN = ArmorMaterial(15, 2, 5, 4, 1, 12, false, Items.AIR)
      @JvmField val IRON = ArmorMaterial(15, 2, 6, 5, 2, 9, false, Items.AIR)
      @JvmField val GOLD = ArmorMaterial(7, 2, 5, 3, 1, 25, false, Items.AIR)
      @JvmField val DIAMOND = ArmorMaterial(33, 3, 8, 6, 3, 10, false, Items.AIR)
   }
   
}
