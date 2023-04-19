package dream.inventory

import dream.collections.ItemList
import dream.item.ItemStack

/**
 * An implementation of [IInventory] that stores [ItemList] items.
 */
interface ItemInventory : IInventory, Iterable<ItemStack> {
   
   /**
    * All items stored on this inventory.
    */
   val items: ItemList
   
   override fun getItem(slot: Int): ItemStack {
      return items[slot]
   }
   
   override fun setItem(slot: Int, item: ItemStack) {
      items.setItem(slot, item, maxStack, ::markDirty)
   }
   
   override fun hasItem(slot: Int): Boolean {
      return !items[slot].isAir
   }
   
   override fun decrease(slot: Int, amount: Int): ItemStack {
      return items.decrease(slot, amount, ::markDirty)
   }
   
   override fun remove(slot: Int): ItemStack {
      return items.removeAt(slot)
   }
   
   override fun clear() = items.clear()
   override fun iterator(): Iterator<ItemStack> = items.iterator()
   
}
