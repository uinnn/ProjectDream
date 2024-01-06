package dream.inventory.slot

import dream.entity.player.*
import dream.inventory.*
import dream.inventory.container.*
import dream.item.*
import dream.misc.*

/**
 * Represents a slot in an inventory.
 *
 * @property inventory The inventory to which the slot belongs.
 * @property index The index of the slot in the inventory.
 */
@Open
class Slot(val inventory: IInventory, val index: Int) {
  
  var rawIndex = 0
  
  /**
   * The item in the slot.
   */
  var item: ItemStack
    get() = inventory[index]
    set(value) {
      inventory[index] = value
      onChange()
    }
  
  /**
   * Checks if the slot has an item.
   */
  val hasItem: Boolean
    get() = !item.isAir
  
  /**
   * The maximum stack size allowed in the slot.
   */
  val slotMaxStack: Int
    get() = inventory.maxStack
  
  /**
   * Called when the item in the slot changes.
   * Marks the inventory as dirty.
   */
  fun onChange() {
    inventory.markDirty()
  }
  
  /**
   * Called when the item in the slot changes compared to another item.
   *
   * @param item The item before the change.
   * @param other The item after the change.
   */
  fun onItemChange(item: ItemStack, other: ItemStack) {
    if (item.its(other.item)) {
      val difference = other.amount - item.amount
      if (difference > 0) {
        onCrafting(item, difference)
      }
    }
  }
  
  /**
   * Called when an item is crafted in the slot.
   *
   * @param item The item that was crafted.
   * @param amount The amount of items crafted.
   */
  fun onCrafting(item: ItemStack, amount: Int) {
    // Implementation specific to handle crafting in the slot
  }
  
  /**
   * Called when an item is crafted in the slot.
   *
   * @param item The item that was crafted.
   */
  fun onCrafting(item: ItemStack) {
    // Implementation specific to handle crafting in the slot
  }
  
  /**
   * Called when a player takes an item from the slot.
   *
   * @param player The player who took the item.
   * @param item The item that was taken.
   */
  fun onTake(player: Player, item: ItemStack) {
    onChange()
  }
  
  /**
   * Checks if an item can be merged into the slot within a container.
   *
   * @param item The item to be merged.
   * @param container The container in which the slot exists.
   * @return `true` if the item can be merged, `false` otherwise.
   */
  fun canMarge(item: ItemStack, container: Container): Boolean {
    // Implementation specific to check if the item can be merged
    return true
  }
  
  /**
   * Checks if a player can take an item from the slot.
   *
   * @param player The player who wants to take the item.
   * @return `true` if the player can take the item, `false` otherwise.
   */
  fun canTake(player: Player): Boolean {
    // Implementation specific to check if the player can take the item
    return true
  }
  
  /**
   * Checks if an item can be placed in the slot.
   *
   * @param item The item to be placed.
   * @return `true` if the item can be placed, `false` otherwise.
   */
  fun canPlace(item: ItemStack): Boolean {
    // Implementation specific to check if the item can be placed
    return true
  }
  
  /**
   * Returns the maximum stack size for a given item in the slot.
   *
   * @param item The item to retrieve the maximum stack size for.
   * @return The maximum stack size for the item.
   */
  fun getMaxStack(item: ItemStack): Int {
    // Implementation specific to retrieve the maximum stack size
    return slotMaxStack
  }
  
  /**
   * Decreases the amount of the item in the slot by the specified amount.
   *
   * @param amount The amount to decrease.
   * @return The resulting item stack after the decrease.
   */
  fun decrease(amount: Int): ItemStack {
    return inventory.decrease(index, amount)
  }
  
  /**
   * Checks if the slot belongs to the specified inventory at the specified slot index.
   *
   * @param inventory The inventory to compare.
   * @param slot The slot index to compare.
   * @return `true` if the slot belongs to the inventory and slot index, `false` otherwise.
   */
  fun isHere(inventory: IInventory, slot: Int): Boolean {
    return this.inventory == inventory && index == slot
  }
  
  /**
   * Handles the click event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who clicked.
   * @param isLeft Indicates if the left mouse button was used for the click.
   * @return The resulting item stack after the click.
   */
  fun onClick(container: Container, player: Player, isLeft: Boolean): ItemStack {
    return stackOf()
  }
  
  /**
   * Handles the shift-click event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who shift-clicked.
   * @param isLeft Indicates if the left mouse button was used for the click.
   * @return The resulting item stack after the shift-click.
   */
  fun onShiftClick(container: Container, player: Player, isLeft: Boolean): ItemStack {
    return stackOf()
  }
  
  /**
   * Handles the hotbar event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who triggered the hotbar event.
   * @param hotbar The index of the hotbar button.
   * @return The resulting item stack after the hotbar event.
   */
  fun onHotbar(container: Container, player: Player, hotbar: Int): ItemStack {
    if (!canTake(player)) return EmptyItemStack
    
    val item = player.getItem(hotbar)
    return stackOf()
  }
  
  /**
   * Handles the middle-click event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who triggered the middle-click.
   * @return The resulting item stack after the middle-click.
   */
  fun onMiddleClick(container: Container, player: Player): ItemStack {
    if (player.consumeItems) {
      item.ifNotAir {
        player.cursor = copy(maxStack)
      }
    }
    
    return EmptyItemStack
  }
  
  /**
   * Handles the drop event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who triggered the drop.
   * @param isCtrl Indicates if the Ctrl key was held during the drop.
   * @return The resulting item stack after the drop.
   */
  fun onDrop(container: Container, player: Player, isCtrl: Boolean): ItemStack {
    return stackOf()
  }
  
  /**
   * Handles the drag event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who triggered the drag.
   * @param button The button used for the drag.
   * @return The resulting item stack after the drag.
   */
  fun onDrag(container: Container, player: Player, button: Int): ItemStack {
    return stackOf()
  }
  
  /**
   * Handles the double-click event on a slot.
   *
   * @param container The container clicked.
   * @param player The player who triggered the double-click.
   * @return The resulting item stack after the double-click.
   */
  fun onDoubleClick(container: Container, player: Player): ItemStack {
    return stackOf()
  }
  
  /**
   * Performs the ticking operation on the container.
   *
   * @param container The container to tick.
   * @param partial The partial tick value.
   */
  fun tick(container: Container, partial: Int) {
    // Implementation specific to handle ticking of the slot
  }
}
