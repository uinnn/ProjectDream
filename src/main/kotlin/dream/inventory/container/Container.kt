package dream.inventory.container

import dream.entity.player.Player
import dream.errors.MinecraftException
import dream.inventory.*
import dream.inventory.slot.Slot
import dream.item.*
import dream.misc.Open

/**
 * A base container.
 */
@Open
abstract class Container : Iterable<Slot> {

   /**
    * The id of this container.
    */
   var id = 0

   /**
    * The transaction id of this container.
    */
   var transactionId: Short = 0

   /**
    * All slots of this container.
    */
   val slots = ArrayList<Slot>()

   /**
    * All remote items of this container.
    */
   val remoteItems = ArrayList<ItemStack>()

   /**
    * Returns all items of this container.
    */
   val items: List<ItemStack>
      get() = slots.map { it.item }

   /**
    * All listeners of this container.
    */
   val listeners = HashSet<ContainerListener>()

   /**
    * Gets all players that is viewing this container.
    */
   val players = ArrayList<Player>()

   /**
    * All slots that a player is current dragging on this container.
    */
   val dragSlots = HashSet<Slot>()

   /**
    * The current drag mode of this container.
    */
   var dragMode = DragMode.SPLIT

   /**
    * The current drag event of this container.
    */
   var dragEvent = DragEvent.START

   /**
    * Gets the tick delay of this container.
    */
   var delay = 1

   /**
    * Checks if the given [player] can interact with this container.
    */
   abstract fun canInteract(player: Player): Boolean

   /**
    * Add [player] to the [players] viewers.
    *
    * @param remove if the player should be removed instead of added.
    */
   fun addViewer(player: Player, remove: Boolean = false) {
      if (remove) {
         players -= player
      } else {
         players += player
      }
   }

   /**
    * Checks if the given [player] is viewing this container.
    */
   fun isViewing(player: Player) = player in players
   operator fun contains(player: Player) = player in players

   /**
    * Add a new listener to this container.
    */
   fun addListener(listener: ContainerListener) {
      listeners += listener
      listener.update(this, items)
      detectChanges()
   }

   /**
    * Remove a listener from this container.
    */
   fun removeListener(listener: ContainerListener) {
      listeners -= listener
   }

   /**
    * Called every tick when [player] has this container open.
    */
   fun tick(player: Player) {
      if (player.ticks % delay != 0)
         return

      detectChanges()
   }

   /**
    * Looks for changes made in this container and updates them.
    */
   fun detectChanges() {
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
      }
   }

   /**
    * Gets a slot on this container.
    */
   fun getSlot(slot: Int) = slots[slot]
   operator fun get(slot: Int) = slots[slot]

   /**
    * Finds a slot by [inventory] and [slot] on this container.
    */
   fun findSlot(inventory: IInventory, slot: Int): Slot? {
      return slots.find { it.isHere(inventory, slot) }
   }

   /**
    * Sets [item] on the given [slot].
    */
   fun setItem(slot: Int, item: ItemStack) {
      slots[slot].item = item
   }

   /**
    * Inserts all [items] on this container.
    *
    * Item will be inserted on the slot at their appear on [items] index.
    */
   fun insert(items: List<ItemStack>) {
      items.forEachIndexed(::setItem)
   }

   /**
    * Returns if a player can drag in the specified [slot].
    */
   fun canDrag(slot: Slot) = true

   /**
    * If the given [slot] is valid for merging with [item] when double clicked.
    */
   fun canMergeSlot(item: ItemStack, slot: Slot): Boolean = slot.canMarge(item, this)

   /**
    *
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
            else -> ItemStack.AIR
         }
      } catch (ex: Throwable) {
         throw MinecraftException("Error while clicking on container.")
               .category("Player Name", player.name)
               .category("Clicked Slot", slot)
               .category("Clicked Button", button)
               .category("Clicked Mode", mode)
      }
   }

   /**
    *
    */
   fun onClick(player: Player, slot: Slot, isLeft: Boolean): ItemStack {
      return stackOf()
   }

   /**
    *
    */
   fun onShiftClick(player: Player, slot: Slot, isLeft: Boolean): ItemStack {
      return stackOf()
   }

   /**
    *
    */
   fun onHotbar(player: Player, slot: Slot, hotbar: Int): ItemStack {
      if (!slot.canTake(player))
         return ItemStack.AIR

      val item = player.getItem(hotbar)
      return stackOf()
   }

   /**
    *
    */
   fun onMiddleClick(player: Player, slot: Slot): ItemStack {
      if (player.isCreative) {
         slot.item.ifNotAir {
            player.cursor = copy(maxStack)
         }
      }

      return ItemStack.AIR
   }

   /**
    *
    */
   fun onDrop(player: Player, slot: Slot, isCtrl: Boolean): ItemStack {
      return stackOf()
   }

   /**
    *
    */
   fun onDrag(player: Player, slot: Int, button: Int): ItemStack {
      return stackOf()
   }

   /**
    *
    */
   fun onDoubleClick(player: Player, slot: Slot): ItemStack {
      return stackOf()
   }

   /**
    *
    */
   fun onOutsideClick(player: Player, isLeft: Boolean): ItemStack {
      return stackOf()
   }

   /**
    * Called when [player] closes this container.
    */
   fun onClose(player: Player) {
      player.cursor.ifNotAir {
         player.drop(this)
         player.cursor = ItemStack.AIR
      }
   }

   /**
    * Called when the crafting matrix has changed.
    */
   fun onCraftMatrixChanged(inventory: IInventory) {
      detectChanges()
   }

   /**
    * Resets the drag data of this container.
    */
   fun resetDrag() {
      dragEvent = DragEvent.START
      dragSlots.clear()
   }

   override fun iterator(): Iterator<Slot> {
      return slots.iterator()
   }
}
