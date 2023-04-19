package dream.item.tool

import dream.item.*
import dream.misc.*

/**
 * Represents a material data for tool items.
 */
@Open
data class ToolMaterial(
   var harvestLevel: Int,
   var maxUses: Int,
   var efficiency: Float,
   var damage: Float,
   var enchantability: Int,
   var repairItem: Item,
) {
   
   companion object {
      @JvmField val WOOD = ToolMaterial(0, 59, 2f, 0f, 15, Items.AIR)
      @JvmField val STONE = ToolMaterial(1, 131, 4f, 1f, 5, Items.AIR)
      @JvmField val IRON = ToolMaterial(2, 250, 6f, 2f, 14, Items.AIR)
      @JvmField val DIAMOND = ToolMaterial(3, 1561, 8f, 3f, 10, Items.AIR)
      @JvmField val GOLD = ToolMaterial(0, 32, 2f, 0f, 22, Items.AIR)
   }
   
}
