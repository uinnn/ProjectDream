@file:Suppress("NOTHING_TO_INLINE")

package dream.inventory

import dream.api.*
import dream.collections.*
import dream.entity.player.*
import dream.item.*

/**
 * Represents an inventory with a collection of ItemStacks.
 */
interface IInventory : Nameable, Iterable<ItemStack> {
  
  /**
   * The list of items in the inventory.
   */
  var items: ItemList
  
  /**
   * The size of the inventory.
   */
  var size: Int
  
  /**
   * The maximum stack size for items in the inventory.
   */
  var maxStack: Int
    get() = items.maxStack
    set(value) {
      items.maxStack = value
    }
  
  /**
   * The number of fields in the inventory.
   */
  val fieldCount: Int
    get() = 0
  
  /**
   * Retrieves the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @return The ItemStack in the slot.
   */
  fun getItem(slot: Int): ItemStack = items.getItem(slot)
  
  /**
   * Sets the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @param item The ItemStack to set.
   */
  fun setItem(slot: Int, item: ItemStack) = items.setItem(slot, item, ::markDirty)
  
  /**
   * Checks if the specified slot contains an item.
   *
   * @param slot The slot index.
   * @return True if the slot contains an item, false otherwise.
   */
  fun hasItem(slot: Int): Boolean = items.hasItem(slot)
  
  /**
   * Decreases the stack size of the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @param amount The amount to decrease.
   * @return The decreased ItemStack.
   */
  fun decrease(slot: Int, amount: Int): ItemStack = items.decrease(slot, amount, ::markDirty)
  
  /**
   * Removes the ItemStack from the specified slot.
   *
   * @param slot The slot index.
   * @return The removed ItemStack.
   */
  fun remove(slot: Int): ItemStack = items.remove(slot)
  
  /**
   * Adds the specified ItemStack, merging if possible.
   *
   * The [item] amount **will not be** subtracted.
   *
   * @param item The ItemStack to add.
   * @return True if the stack was successfully added, false otherwise.
   */
  fun add(item: ItemStack) = items.add(item, ::markDirty)
  
  /**
   * Picks up the specified ItemStack, merging if possible.
   *
   * The [item] amount **will be** subtracted.
   *
   * @param item The ItemStack to pick up.
   * @return True if the stack was successfully picked up, false otherwise.
   */
  fun pickup(item: ItemStack) = items.pickup(item, ::markDirty)
  
  /**
   * Adds an ItemStack to the ItemList without merging.
   *
   * @param item The ItemStack to add.
   * @return `true` if the ItemStack was added successfully, `false` if there is no empty slot.
   */
  fun addWithoutMerge(item: ItemStack) = items.addWithoutMerge(item, ::markDirty)
  
  /**
   * Marks the inventory as dirty, indicating that it has been modified.
   */
  fun markDirty()
  
  /**
   * Checks if the inventory can be used by the specified player.
   *
   * @param player The player using the inventory.
   * @return True if the inventory can be used, false otherwise.
   */
  fun isUseable(player: Player): Boolean = true
  
  /**
   * Called when the inventory is opened by a player.
   *
   * @param player The player who opened the inventory.
   */
  fun onOpen(player: Player) = Unit
  
  /**
   * Called when the inventory is closed by a player.
   *
   * @param player The player who closed the inventory.
   */
  fun onClose(player: Player) = Unit
  
  /**
   * Checks if the specified item is valid for the specified slot.
   *
   * @param slot The slot index.
   * @param item The ItemStack to check.
   * @return True if the item is valid for the slot, false otherwise.
   */
  fun isSlotValid(slot: Int, item: ItemStack): Boolean = true
  
  /**
   * Clears the inventory, removing all items.
   */
  fun clear() = items.clear()
  
  /**
   * Retrieves the value of the field with the specified ID.
   *
   * @param id The ID of the field.
   * @return The value of the field.
   */
  fun getField(id: Int): Int = 0
  
  /**
   * Sets the value of the field with the specified ID.
   *
   * @param id The ID of the field.
   * @param value The value to set.
   */
  fun setField(id: Int, value: Int) = Unit
  
  /**
   * Retrieves all non-air ItemStacks in the collection.
   *
   * @return A list of non-air ItemStacks.
   */
  fun allNonAir(): List<ItemStack> = items.allNonAir()
  
  /**
   * Retrieves all ItemStacks matching the specified ItemStack, with optional tag and stack checking.
   *
   * @param item The ItemStack to match.
   * @param checkTag Indicates whether to check the tags of the ItemStacks.
   * @param checkStack Indicates whether to check the stack size of the ItemStacks.
   * @return A list of matching ItemStacks.
   */
  fun all(item: ItemStack, checkTag: Boolean = true, checkStack: Boolean = true): List<ItemStack> {
    return items.all(item, checkTag, checkStack)
  }
  
  /**
   * Retrieves all ItemStacks matching the specified Item.
   *
   * @param item The Item to match.
   * @return A list of matching ItemStacks.
   */
  fun all(item: Item): List<ItemStack> = items.all(item)
  
  /**
   * Retrieves the index of the first slot containing a partially filled ItemStack matching the specified ItemStack.
   *
   * @param item The ItemStack to match.
   * @return The index of the first partial slot, or -1 if not found.
   */
  fun firstPartial(item: ItemStack): Int {
    return items.firstPartial(item)
  }
  
  /**
   * Retrieves the index of the first slot containing an ItemStack matching the specified ItemStack,
   * with optional tag and stack checking.
   *
   * @param item The ItemStack to match.
   * @param checkTag Indicates whether to check the tags of the ItemStacks.
   * @param checkStack Indicates whether to check the stack size of the ItemStacks.
   * @return The index of the first matching slot, or -1 if not found.
   */
  fun firstSlot(item: ItemStack, checkTag: Boolean = true, checkStack: Boolean = true): Int {
    return items.firstSlot(item, checkTag, checkStack)
  }
  
  /**
   * Retrieves the index of the first empty slot in the collection.
   *
   * @return The index of the first empty slot, or -1 if not found.
   */
  fun firstEmptySlot(): Int = items.firstEmptySlot()
  
  /**
   * Retrieves the index of the slot containing the specified ItemStack.
   *
   * @param item The ItemStack to search for.
   * @return The index of the slot containing the ItemStack, or -1 if not found.
   */
  fun slotOf(item: ItemStack): Int = items.slotOf(item)
  
  /**
   * Returns an iterator over the ItemStacks in the inventory.
   *
   * @return An iterator over the ItemStacks.
   */
  override fun iterator(): Iterator<ItemStack> = items.iterator()
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
