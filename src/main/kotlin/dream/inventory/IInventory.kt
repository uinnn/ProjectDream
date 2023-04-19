@file:Suppress("NOTHING_TO_INLINE")

package dream.inventory

import dream.entity.player.*
import dream.interfaces.*
import dream.item.*

/**
 * Represents a base interface for all inventories.
 */
interface IInventory : Nameable, Iterable<ItemStack> {
   
   /**
    * Gets the size of this inventory.
    */
   val size: Int
   
   /**
    * Gets the max stack of this inventory.
    */
   val maxStack: Int get() = 64
   
   /**
    * Gets the amount of field of this inventory.
    */
   val fieldCount: Int get() = 0
   
   /**
    * Gets an item on the specified [slot].
    */
   fun getItem(slot: Int): ItemStack
   
   /**
    * Sets an item on the specified [slot].
    */
   fun setItem(slot: Int, item: ItemStack)
   
   /**
    * Checks if this inventory has an item on [slot].
    */
   fun hasItem(slot: Int): Boolean
   
   /**
    * Decreases the item stack on the given [slot] in [amount] and returns the split one.
    */
   fun decrease(slot: Int, amount: Int): ItemStack
   
   /**
    * Remove an item from the given [slot].
    */
   fun remove(slot: Int): ItemStack
   
   /**
    * Marks this inventory dirty.
    *
    * This is, call all [InventoryListener] of this inventory.
    */
   fun markDirty()
   
   /**
    * Checks if [player] can use this inventory.
    */
   fun isUseable(player: Player): Boolean = true
   
   /**
    * Called when [player] opens this inventory.
    */
   fun onOpen(player: Player) = Unit
   
   /**
    * Called when [player] close this inventory.
    */
   fun onClose(player: Player) = Unit
   
   /**
    * Checks if the given [item] is valid to the given [slot].
    */
   fun isSlotValid(slot: Int, item: ItemStack): Boolean = true
   
   /**
    * Clear this inventory.
    */
   fun clear()
   
   /**
    * Gets a field data from this inventory by given [id].
    */
   fun getField(id: Int): Int = 0
   
   /**
    * Sets a field data of this inventory from given [id] by [value].
    */
   fun setField(id: Int, value: Int) = Unit
}

/**
 * Gets an item on the specified [slot].
 */
inline operator fun IInventory.get(slot: Int): ItemStack = getItem(slot)

/**
 * Sets an item on the specified [slot].
 */
inline operator fun IInventory.set(slot: Int, item: ItemStack) = setItem(slot, item)

/**
 * Checks if this inventory has an item on [slot].
 */
inline operator fun IInventory.contains(slot: Int) = hasItem(slot)
