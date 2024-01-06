package dream.inventory

import dream.inventory.container.*
import dream.item.*

/**
 * Represents a listener for [Container].
 */
interface ContainerListener {

  fun update(container: Container, items: List<ItemStack>)

  fun sendContent(container: Container, slot: Int, item: ItemStack)

  fun sendData(container: Container, id: Int, value: Int)

  fun sendProps(container: Container, inventory: IInventory)

}
