package dream.inventory

import dream.collections.*
import dream.misc.*

/**
 * Represents a basic inventory.
 */
@Open
class Inventory(override var name: String, override val size: Int) : ItemInventory {
   
   override val items = ItemList(size)
   
   /**
    * All listeners of this inventory.
    */
   val listeners = HashSet<InventoryListener>()
   
   /**
    * Adds [listener] to be listened on this inventory.
    */
   fun addListener(listener: InventoryListener) {
      listeners += listener
   }
   
   /**
    * Removes [listener] from this inventory listeners.
    */
   fun removeListener(listener: InventoryListener) {
      listeners -= listener
   }
   
   /**
    * Marks this inventory dirty.
    *
    * This is, call all [InventoryListener] of this inventory.
    */
   override fun markDirty() {
      if (listeners.isEmpty())
         return
      
      for (listener in listeners) {
         listener.onChanged(this)
      }
   }
}
