package dream.inventory

import dream.entity.player.*
import dream.item.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*

/**
 * Represents an inventory of a player.
 */
@Open
class PlayerInventory(val player: Player) : IInventory, CompoundStorable {
   
   /**
    * Represents the main items on this inventory.
    */
   val items = ArrayList<ItemStack>()
   
   /**
    * Represents the armor items on this inventory.
    */
   val armors = ArrayList<ItemStack>()
   
   /**
    * The current held slot.
    */
   var heldSlot = 0
      set(value) {
         field = value.coerceIn(0, 8)
      }
   
   /**
    * The item that's the player is holding.
    */
   var heldItem: ItemStack
      get() = items[heldSlot]
      set(value) {
         items[heldSlot] = value
      }
   
   /**
    * The item on the cursor.
    */
   var cursor = ItemStack.AIR
   
   /**
    * Gets if this inventory has changed.
    */
   var hasChanged = false
   
   /**
    * Gets the name of this inventory.
    */
   override var name = "Inventory"
   
   /**
    * Gets the size of this inventory.
    */
   override val size get() = items.size + 4
   
   /**
    * Gets the amount of field of this inventory.
    */
   override val fieldCount get() = 0
   
   /**
    * Gets the max stack of this inventory.
    */
   override val maxStack get() = 64
   
   /**
    * Gets the sum of all defense gived by armors.
    */
   val armorDefense: Int
      get() = armors.sumOf { it.armor?.defense ?: 0 }
   
   /**
    * Gets all items of this inventory based on the given [slot].
    */
   fun getItemsBasedOnSlot(slot: Int): ArrayList<ItemStack> {
      return if (slot >= items.size) return armors else items
   }
   
   /**
    * Drops all items of this inventory.
    */
   fun dropInventory() {
      items.forEachIndexed { index, item ->
         if (!item.isAir) {
            player.drop(item, true)
            items[index] = ItemStack.AIR
         }
      }
   }
   
   /**
    * Drops the armor of this inventory.
    */
   fun dropArmor() {
      armors.forEachIndexed { index, item ->
         if (!item.isAir) {
            player.drop(item, true)
            armors[index] = ItemStack.AIR
         }
      }
   }
   
   /**
    * Drops everything of this inventory, including items and armors.
    */
   fun dropAll() {
      dropInventory()
      dropArmor()
   }
   
   /**
    * Gets an item on the specified [slot].
    */
   override fun getItem(slot: Int): ItemStack {
      val size = items.size
      return if (slot >= size) getArmor(slot - size) else items[slot]
   }
   
   /**
    * Sets an item on the specified [slot].
    */
   override fun setItem(slot: Int, item: ItemStack) {
      val size = items.size
      if (slot >= size) {
         setArmor(slot - size, item)
      } else {
         items[slot] = item
      }
   }
   
   /**
    * Checks if this inventory has an item on [slot].
    */
   override fun hasItem(slot: Int): Boolean {
      return !getItem(slot).isAir
   }
   
   /**
    * Decreases the item stack on the given [slot] in [amount] and returns the split one.
    */
   override fun decrease(slot: Int, amount: Int): ItemStack {
      val item = getItem(slot)
      
      return if (item.amount < amount) {
         remove(slot)
         item
      } else {
         val split = item.split(amount)
         if (item.amount == 0) {
            remove(slot)
         }
         
         split
      }
   }
   
   /**
    * Remove an item from the given [slot].
    */
   override fun remove(slot: Int): ItemStack {
      val size = items.size
      return if (slot >= size) {
         setArmor(slot - size, ItemStack.AIR)
      } else {
         items.set(slot, ItemStack.AIR)
      }
   }
   
   /**
    * Gets an armor at the given [slot]
    */
   fun getArmor(slot: Int) = armors[slot % 4]
   
   /**
    * Sets an armor at the given [slot].
    */
   fun setArmor(slot: Int, item: ItemStack) = armors.set(slot % 4, item)
   
   /**
    * Marks this inventory dirty.
    */
   override fun markDirty() {
      hasChanged = true
   }
   
   /**
    * Checks if [player] can use this inventory.
    */
   override fun isUseable(player: Player): Boolean {
      return player.isAlive
   }
   
   /**
    * Called when [player] opens this inventory.
    */
   override fun onOpen(player: Player) {
   }
   
   /**
    * Called when [player] close this inventory.
    */
   override fun onClose(player: Player) {
   }
   
   /**
    * Checks if the given [item] is valid to the given [slot].
    */
   override fun isSlotValid(slot: Int, item: ItemStack): Boolean {
      return true
   }
   
   /**
    * Clear this inventory.
    */
   override fun clear() {
      items.fill(ItemStack.AIR)
      armors.fill(ItemStack.AIR)
   }
   
   /**
    * Gets a field data from this inventory by given [id].
    */
   override fun getField(id: Int): Int {
      return 0
   }
   
   /**
    * Sets a field data of this inventory from given [id] by [value].
    */
   override fun setField(id: Int, value: Int) {
   }
   
   override fun save(tag: CompoundTag) {
      TODO("Not yet implemented")
   }
   
   override fun load(tag: CompoundTag) {
      TODO("Not yet implemented")
   }
   
   override fun iterator(): Iterator<ItemStack> {
      return (items + armors).iterator()
   }
}
