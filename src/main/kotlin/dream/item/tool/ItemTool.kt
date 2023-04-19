package dream.item.tool

import dream.item.*
import dream.tab.*

/**
 * Represents a tool item.
 */
class ItemTool(
   var material: ToolMaterial = ToolMaterial.WOOD,
) : Item(CreativeTab.TOOLS) {
   
   /**
    * Gets the harvest level of this tool.
    */
   val harvestLevel get() = material.harvestLevel
   
   /**
    * Gets the max uses of this tool.
    */
   val maxUses get() = material.maxUses
   
   /**
    * Gets the efficiency of this tool.
    */
   val efficiency get() = material.efficiency
   
   /**
    * Gets the damage of this tool.
    */
   val damage get() = material.damage
   
   override fun getEnchantability(): Int {
      return material.enchantability
   }
}
