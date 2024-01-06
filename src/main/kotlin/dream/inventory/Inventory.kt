package dream.inventory

import dream.collections.*
import dream.misc.*

/**
 * Represents an inventory implementation that implements the [IInventory] interface.
 *
 * @param name The name of the inventory.
 * @param items The list of items in the inventory.
 */
@Open
class Inventory(override var name: String, override var items: ItemList) : IInventory {
  
  /**
   * Secondary constructor that creates an inventory with the specified name and size.
   *
   * @param name The name of the inventory.
   * @param size The size of the inventory.
   */
  constructor(name: String, size: Int) : this(name, ItemList(size))
  
  /**
   * The size of the inventory.
   */
  override var size: Int = items.size
    set(value) {
      if (field != value) {
        items.rearrangeSize(value)
        field = value
      }
    }
  
  /**
   * The set of listeners attached to the inventory.
   */
  private val listeners = HashSet<InventoryListener>()
  
  /**
   * Adds a listener to the inventory.
   *
   * @param listener The listener to add.
   */
  fun addListener(listener: InventoryListener) {
    listeners += listener
  }
  
  /**
   * Removes a listener from the inventory.
   *
   * @param listener The listener to remove.
   */
  fun removeListener(listener: InventoryListener) {
    listeners -= listener
  }
  
  /**
   * Marks the inventory as dirty, indicating that it has been modified.
   * Notifies all attached listeners of the change.
   */
  override fun markDirty() {
    if (listeners.isNotEmpty()) {
      listeners.forEach {
        it.onChanged(this)
      }
    }
  }
}
