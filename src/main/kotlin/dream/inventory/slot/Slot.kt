package dream.inventory.slot

import dream.entity.player.Player
import dream.inventory.*
import dream.inventory.container.Container
import dream.item.ItemStack
import dream.misc.Open

/**
 * Represents a slot of an inventory.
 */
@Open
class Slot(val inventory: IInventory, val index: Int, val x: Int, val y: Int) {
   
   /**
    * The raw index of this slot.
    */
   var rawIndex = 0
   
   /**
    * Returns the item present on this slot.
    */
   var item: ItemStack
      get() = inventory[index]
      set(value) {
         inventory[index] = value
         onChange()
      }
   
   /**
    * Checks if this slot has an item present.
    */
   val hasItem: Boolean
      get() = !item.isAir
   
   /**
    * Gets the max stack acceptable for this slot.
    */
   val slotMaxStack: Int
      get() = inventory.maxStack
   
   /**
    * Called when this slot changes.
    */
   fun onChange() {
      inventory.markDirty()
   }
   
   /**
    * Called when this slot item changes.
    */
   fun onItemChange(item: ItemStack, other: ItemStack) {
      if (item.item != other.item)
         return
      
      val difference = other.amount - item.amount
      if (difference > 0) {
         onCrafting(item, difference)
      }
   }
   
   /**
    * Called when [item] is passed on output.
    */
   fun onCrafting(item: ItemStack, amount: Int) {
   }
   
   /**
    * Called when [item] is passed on output.
    */
   fun onCrafting(item: ItemStack) {
   }
   
   /**
    * Called when [player] takes the [item] on this slot.
    */
   fun onTake(player: Player, item: ItemStack) {
      onChange()
   }
   
   /**
    * Checks to see if this slot is valid for merging [item] on [container].
    */
   fun canMarge(item: ItemStack, container: Container): Boolean {
      return true
   }
   
   /**
    * Checks if [player] can take the item on this slot.
    */
   fun canTake(player: Player): Boolean {
      return true
   }
   
   /**
    * Checks if [item] is valid to place on this slot.
    */
   fun canPlace(item: ItemStack): Boolean {
      return true
   }
   
   /**
    * Gets the max stack for the given [item] on this slot.
    */
   fun getMaxStack(item: ItemStack): Int {
      return slotMaxStack
   }
   
   /**
    * Decrease the item on this slot by given [amount].
    */
   fun decrease(amount: Int): ItemStack {
      return inventory.decrease(index, amount)
   }
   
   /**
    * Checks if this slot is in [inventory] and in [slot].
    */
   fun isHere(inventory: IInventory, slot: Int): Boolean {
      return this.inventory == inventory && index == slot
   }
}
