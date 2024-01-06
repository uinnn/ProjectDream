package dream.inventory.container

import dream.entity.player.*
import dream.errors.*
import dream.inventory.*
import dream.inventory.slot.*
import dream.item.*
import dream.misc.*

/**
 * Represents an abstract container that contains slots and provides common functionality.
 */
@Open
abstract class Container : Iterable<Slot> {
  
  /**
   * The ID of the container.
   */
  var id = 0
  
  /**
   * The transaction ID of the container.
   */
  var transactionId: Short = 0
  
  /**
   * The list of slots in the container.
   */
  val slots = ArrayList<Slot>()
  
  /**
   * The list of remote items in the container.
   */
  val remoteItems = ArrayList<ItemStack>()
  
  /**
   * The list of items in the container.
   */
  val items: List<ItemStack>
    get() = slots.map { it.item }
  
  /**
   * The set of listeners attached to the container.
   */
  val listeners = HashSet<ContainerListener>()
  
  /**
   * The list of players associated with the container.
   */
  val players = HashSet<Player>()
  
  /**
   * The set of slots involved in a drag operation.
   */
  val dragSlots = HashSet<Slot>()
  
  /**
   * The drag mode for the container.
   */
  var dragMode = DragMode.SPLIT
  
  /**
   * The drag event type for the container.
   */
  var dragEvent = DragEvent.START
  
  /**
   * The delay value for the container.
   */
  var delay = 1
  
  /**
   * Checks if the player can interact with the container.
   *
   * @param player The player.
   * @return `true` if the player can interact, `false` otherwise.
   */
  abstract fun canInteract(player: Player): Boolean
  
  /**
   * Adds a viewer to the container.
   *
   * @param player The player to add as a viewer.
   * @param remove If `true`, removes the player from the viewers.
   */
  fun addViewer(player: Player, remove: Boolean = false) {
    if (remove) {
      players -= player
    } else {
      players += player
    }
  }
  
  /**
   * Checks if a player is viewing the container.
   *
   * @param player The player to check.
   * @return `true` if the player is viewing, `false` otherwise.
   */
  fun isViewing(player: Player) = player in players
  
  /**
   * Checks if a player is contained in the container.
   *
   * @param player The player to check.
   * @return `true` if the player is contained, `false` otherwise.
   */
  operator fun contains(player: Player) = player in players
  
  /**
   * Adds a listener to the container.
   *
   * @param listener The listener to add.
   */
  fun addListener(listener: ContainerListener) {
    listeners += listener
    listener.update(this, items)
    detectChanges()
  }
  
  /**
   * Removes a listener from the container.
   *
   * @param listener The listener to remove.
   */
  fun removeListener(listener: ContainerListener) {
    listeners -= listener
  }
  
  /**
   * Performs a tick on the container.
   *
   * @param player The player associated with the container.
   * @param partial The tick partial value.
   */
  fun tick(player: Player, partial: Int) {
    if (partial % delay == 0) {
      detectChanges(partial)
    }
  }
  
  /**
   * Detects changes in the container and notifies listeners.
   */
  fun detectChanges(partial: Int = -1) {
    slots.forEachIndexed { index, slot ->
      val slotItem = slot.item
      var remoteItem = remoteItems[index]
      if (!remoteItem.isSimilar(slotItem)) {
        remoteItem = slotItem.copy()
        remoteItems[index] = remoteItem
        for (listener in listeners) {
          listener.sendContent(this, index, remoteItem)
        }
      }
      
      slot.tick(this, partial)
    }
  }
  
  /**
   * Gets the slot at the specified index.
   *
   * @param slot The index of the slot.
   * @return The slot at the specified index.
   */
  fun getSlot(slot: Int) = slots[slot]
  
  /**
   * Gets the slot at the specified index using the indexing operator.
   *
   * @param slot The index of the slot.
   * @return The slot at the specified index.
   */
  operator fun get(slot: Int) = slots[slot]
  
  /**
   * Finds a slot in the container based on the inventory and slot index.
   *
   * @param inventory The inventory to search in.
   * @param slot The slot index.
   * @return The found slot, or `null` if not found.
   */
  fun findSlot(inventory: IInventory, slot: Int): Slot? {
    return slots.find { it.isHere(inventory, slot) }
  }
  
  /**
   * Sets the item in the specified slot.
   *
   * @param slot The index of the slot.
   * @param item The item to set.
   */
  fun setItem(slot: Int, item: ItemStack) {
    slots[slot].item = item
  }
  
  /**
   * Inserts multiple items into the container.
   *
   * @param items The items to insert.
   */
  fun insert(items: Iterable<ItemStack>) {
    items.forEachIndexed(::setItem)
  }
  
  /**
   * Determines if a slot can be dragged.
   *
   * @param slot The slot to check.
   * @return `true` if the slot can be dragged, `false` otherwise.
   */
  fun canDrag(slot: Slot) = true
  
  /**
   * Checks if an item can be merged into a slot.
   *
   * @param item The item to be merged.
   * @param slot The slot to merge the item into.
   * @return `true` if the item can be merged, `false` otherwise.
   */
  fun canMergeSlot(item: ItemStack, slot: Slot): Boolean = slot.canMarge(item, this)
  
  /**
   * Handles the click event on a slot.
   *
   * @param player The player who clicked.
   * @param slot The index of the clicked slot.
   * @param button The button used for the click.
   * @param mode The click mode.
   * @return The resulting item stack after the click.
   */
  fun clicked(player: Player, slot: Int, button: Int, mode: ClickMode): ItemStack {
    return try {
      when {
        mode == ClickMode.MOUSE -> onClick(player, getSlot(slot), button == 0)
        mode == ClickMode.SHIFT_MOUSE -> onShiftClick(player, getSlot(slot), button == 0)
        mode == ClickMode.HOTBAR -> onHotbar(player, getSlot(slot), button)
        mode == ClickMode.MIDDLE -> onMiddleClick(player, getSlot(slot))
        mode == ClickMode.DROP -> onDrop(player, getSlot(slot), button == 1)
        mode == ClickMode.DRAG -> onDrag(player, slot, button) // slot can be -999 if drag is start/over
        mode == ClickMode.DOUBLE_CLICK -> onDoubleClick(player, getSlot(slot))
        slot == -999 -> onOutsideClick(player, button == 0)
        else -> EmptyItemStack
      }
    } catch (ex: Throwable) {
      throw MinecraftException("Error while clicking on container.").category("Player", player)
        .category("Clicked Slot", slot).category("Clicked Button", button).category("Clicked Mode", mode)
    }
  }
  
  /**
   * Handles the click event on a slot.
   *
   * @param player The player who clicked.
   * @param slot The clicked slot.
   * @param isLeft Indicates if the left mouse button was used for the click.
   * @return The resulting item stack after the click.
   */
  fun onClick(player: Player, slot: Slot, isLeft: Boolean): ItemStack {
    return slot.onClick(this, player, isLeft)
  }
  
  /**
   * Handles the shift-click event on a slot.
   *
   * @param player The player who shift-clicked.
   * @param slot The clicked slot.
   * @param isLeft Indicates if the left mouse button was used for the click.
   * @return The resulting item stack after the shift-click.
   */
  fun onShiftClick(player: Player, slot: Slot, isLeft: Boolean): ItemStack {
    return slot.onShiftClick(this, player, isLeft)
  }
  
  /**
   * Handles the hotbar event on a slot.
   *
   * @param player The player who triggered the hotbar event.
   * @param slot The clicked slot.
   * @param hotbar The index of the hotbar button.
   * @return The resulting item stack after the hotbar event.
   */
  fun onHotbar(player: Player, slot: Slot, hotbar: Int): ItemStack {
    return slot.onHotbar(this, player, hotbar)
  }
  
  /**
   * Handles the middle-click event on a slot.
   *
   * @param player The player who triggered the middle-click.
   * @param slot The clicked slot.
   * @return The resulting item stack after the middle-click.
   */
  fun onMiddleClick(player: Player, slot: Slot): ItemStack {
    return slot.onMiddleClick(this, player)
  }
  
  /**
   * Handles the drop event on a slot.
   *
   * @param player The player who triggered the drop.
   * @param slot The clicked slot.
   * @param isCtrl Indicates if the Ctrl key was held during the drop.
   * @return The resulting item stack after the drop.
   */
  fun onDrop(player: Player, slot: Slot, isCtrl: Boolean): ItemStack {
    return slot.onDrop(this, player, isCtrl)
  }
  
  /**
   * Handles the drag event on a slot.
   *
   * @param player The player who triggered the drag.
   * @param slot The index of the clicked slot.
   * @param button The button used for the drag.
   * @return The resulting item stack after the drag.
   */
  fun onDrag(player: Player, slot: Int, button: Int): ItemStack {
    return when (slot) {
      -999 -> {}
      else -> getSlot(slot).onDrag(this, player, button)
    }
  }
  
  /**
   * Handles the double-click event on a slot.
   *
   * @param player The player who triggered the double-click.
   * @param slot The clicked slot.
   * @return The resulting item stack after the double-click.
   */
  fun onDoubleClick(player: Player, slot: Slot): ItemStack {
    return slot.onDoubleClick(this, player)
  }
  
  /**
   * Handles the click event outside the container.
   *
   * @param player The player who triggered the outside click.
   * @param isLeft Indicates if the left mouse button was used for the click.
   * @return The resulting item stack after the outside click.
   */
  fun onOutsideClick(player: Player, isLeft: Boolean): ItemStack {
    return stackOf()
  }
  
  /**
   * Handles the close event of the container.
   *
   * @param player The player who closed the container.
   */
  fun onClose(player: Player) {
    player.cursor.ifNotAir {
      player.drop(this)
      player.cursor = EmptyItemStack
    }
  }
  
  /**
   * Handles the change event of the craft matrix inventory.
   *
   * @param inventory The craft matrix inventory.
   */
  fun onCraftMatrixChanged(inventory: IInventory) {
    detectChanges()
  }
  
  /**
   * Resets the drag event and clears the drag slots.
   */
  fun resetDrag() {
    dragEvent = DragEvent.START
    dragSlots.clear()
  }
  
  /**
   * Returns an iterator over the slots in the container.
   *
   * @return An iterator over the slots.
   */
  override fun iterator(): Iterator<Slot> = slots.iterator()
}
