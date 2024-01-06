package dream.inventory

/**
 * Represents a listener for [IInventory].
 */
fun interface InventoryListener {

  fun onChanged(inventory: Inventory)

}
