package dream.inventory

import com.soywiz.kmem.*
import dream.collections.*
import dream.entity.player.*
import dream.item.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*

/**
 * Represents the player's inventory.
 */
@Open
class PlayerInventory(val player: Player) : IInventory, ListStorable<CompoundTag> {
  
  /**
   * The items in the inventory.
   */
  override var items = ItemList(36)
  
  /**
   * The armors items.
   */
  val armors = object : ItemList(4) {
    override fun setItem(index: Int, stack: ItemStack, whenDone: Runnable?) {
      super.setItem(index % 4, stack, whenDone)
    }
    
    override fun getItem(index: Int): ItemStack {
      return super.getItem(index % 4)
    }
    
    override fun remove(index: Int, whenDone: Runnable?): ItemStack {
      return super.remove(index % 4, whenDone)
    }
    
    override fun save(tag: ListTag<CompoundTag>) {
      items.forEachIndexed { index, item ->
        if (!item.isAir) {
          val data = item.store()
          data["Slot"] = (index + 100).toByte()
          tag.add(data)
        }
      }
    }
    
    override fun load(tag: ListTag<CompoundTag>) {
      for (data in tag) {
        val slot = (data.byte("Slot") and 255).toInt()
        items[slot - 100] = data.createItem()
      }
    }
  }
  
  /**
   * The fully inventory, containing [items] and [armors].
   */
  val inventory = (items.items + armors.items).asList()
  
  /**
   * The currently held slot index.
   */
  var heldSlot = 0
    set(value) {
      field = value.clamp(0, 8)
    }
  
  /**
   * The item currently held in the player's hand.
   */
  var heldItem: ItemStack
    get() = items[heldSlot]
    set(value) {
      items.setItem(heldSlot, value)
    }
  
  /**
   * The item currently on the cursor.
   */
  var cursor: ItemStack = EmptyItemStack
  
  /**
   * Flag indicating whether the inventory has changed.
   */
  var hasChanged = false
  
  /**
   * The name of the inventory.
   */
  override var name = "Inventory"
  
  /**
   * The size of the inventory.
   */
  override var size
    get() = items.size + armors.size
    set(value) = error("Cannot change the size of a Player Inventory.")
  
  /**
   * The number of fields in the inventory.
   */
  override val fieldCount get() = 0
  
  /**
   * The total defense value provided by the equipped armor pieces.
   */
  val armorDefense: Int get() = armors.sumOf { it.armor?.defense ?: 0 }
  
  /**
   * Returns the item list based on the given slot index.
   *
   * @param slot The slot index.
   * @return The item list associated with the slot index.
   */
  fun getItemsBasedOnSlot(slot: Int): ItemList {
    return if (slot >= items.size) return armors else items
  }
  
  /**
   * Retrieves the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @return The ItemStack in the slot.
   */
  override fun getItem(slot: Int): ItemStack {
    return getItemsBasedOnSlot(slot).getItem(slot)
  }
  
  /**
   * Sets the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @param item The ItemStack to set.
   */
  override fun setItem(slot: Int, item: ItemStack) {
    getItemsBasedOnSlot(slot).setItem(slot, item)
  }
  
  /**
   * Checks if the specified slot contains an item.
   *
   * @param slot The slot index.
   * @return True if the slot contains an item, false otherwise.
   */
  override fun hasItem(slot: Int): Boolean {
    return getItemsBasedOnSlot(slot).hasItem(slot)
  }
  
  /**
   * Decreases the stack size of the ItemStack in the specified slot.
   *
   * @param slot The slot index.
   * @param amount The amount to decrease.
   * @return The decreased ItemStack.
   */
  override fun decrease(slot: Int, amount: Int): ItemStack {
    return getItemsBasedOnSlot(slot).decrease(slot, amount)
  }
  
  /**
   * Removes the ItemStack from the specified slot.
   *
   * @param slot The slot index.
   * @return The removed ItemStack.
   */
  override fun remove(slot: Int): ItemStack {
    return getItemsBasedOnSlot(slot).remove(slot)
  }
  
  /**
   * Retrieves the armor item at the specified slot.
   *
   * @param slot The armor slot index.
   * @return The armor item at the specified slot.
   */
  fun getArmor(slot: Int) = armors.getItem(slot)
  
  /**
   * Sets the armor item at the specified slot.
   *
   * @param slot The armor slot index.
   * @param item The item to set.
   */
  fun setArmor(slot: Int, item: ItemStack) = armors.setItem(slot, item)
  
  /**
   * Removes the armor item at the specified slot.
   *
   * @param slot The armor slot index.
   * @return The removed armor item.
   */
  fun removeArmor(slot: Int) = armors.remove(slot)
  
  /**
   * Marks the inventory as dirty, indicating that it has been modified.
   */
  override fun markDirty() {
    hasChanged = true
  }
  
  /**
   * Checks if the inventory can be used by the specified player.
   *
   * @param player The player using the inventory.
   * @return True if the inventory can be used, false otherwise.
   */
  override fun isUseable(player: Player): Boolean {
    return !this.player.isDead && player.isNear(this.player, 8.0)
  }
  
  /**
   * Clears the inventory, removing all items.
   */
  override fun clear() {
    items.clear()
    armors.clear()
  }
  
  /**
   * Drops all items in the inventory to the ground.
   */
  fun dropInventory() {
    items.forEachIndexed { index, item ->
      if (!item.isAir) {
        player.drop(item, true)
        items.remove(index)
      }
    }
  }
  
  /**
   * Drops all equipped armor items to the ground.
   */
  fun dropArmor() {
    armors.forEachIndexed { index, item ->
      if (!item.isAir) {
        player.drop(item, true)
        armors.remove(index)
      }
    }
  }
  
  /**
   * Drops all items in the inventory and equipped armor items to the ground.
   */
  fun dropAll() {
    dropInventory()
    dropArmor()
  }
  
  override fun save(tag: ListTag<CompoundTag>) {
    items.save(tag)
    armors.save(tag)
  }
  
  override fun load(tag: ListTag<CompoundTag>) {
    for (data in tag) {
      val slot = (data.byte("Slot") and 255).toInt()
      val item = data.createItem()
      if (!item.isAir) {
        when {
          slot < items.size -> items.setItem(slot, item)
          slot >= 100 && slot < armors.size + 100 -> armors.setItem(slot - 100, item)
        }
      }
    }
  }
  
  override fun iterator(): Iterator<ItemStack> {
    return inventory.iterator()
  }
}
