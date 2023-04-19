package dream.inventory

import dream.entity.player.*
import dream.item.*
import dream.utils.*

/**
 * Represents all drag mode when dragging an item on a container.
 */
enum class DragMode(val id: Int) {
   
   /**
    * Will splits items by the given amount of slot.
    */
   SPLIT(0) {
      override fun computeStack(item: ItemStack, slots: Int, slotStack: Int) {
         item.amount = floorInt(item.amount.toFloat() / slots.toFloat()) + slotStack
      }
   },
   
   /**
    * Will splits items one by one.
    */
   ONE(1) {
      override fun computeStack(item: ItemStack, slots: Int, slotStack: Int) {
         item.amount = 1 + slotStack
      }
   },
   
   /**
    * Will splits items with their max stack.
    *
    * Used in creative mode.
    */
   FULL(2) {
      override fun isValid(player: Player) = player.isCreative
      override fun computeStack(item: ItemStack, slots: Int, slotStack: Int) {
         item.amount = item.maxStack + slotStack
      }
   };
   
   /**
    * Checks if this drag mode is valid for [player].
    */
   open fun isValid(player: Player) = true
   
   /**
    * Computes the new stack size of [item] for this drag mode.
    */
   abstract fun computeStack(item: ItemStack, slots: Int, slotStack: Int)
   
   companion object {
      
      /**
       * Gets a drag mode by [id].
       */
      fun byId(id: Int) = when (id shr 2 and 3) {
         1 -> ONE
         2 -> FULL
         else -> SPLIT
      }
   }
}
