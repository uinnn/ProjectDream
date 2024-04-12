package dream.entity.player

import dream.*
import dream.block.*
import dream.chat.*
import dream.command.*
import dream.entity.base.*
import dream.inventory.*
import dream.inventory.container.*
import dream.item.*
import dream.item.food.*
import dream.level.Level
import dream.network.*
import dream.pos.*
import dream.server.*
import dream.utils.*
import java.net.*

/**
 * Represents a player.
 */
class Player(
  val profile: Profile,
  var server: Server,
  var connection: PlayerConnection,
  var interaction: PlayerInteraction,
) : EntityLiving(), ContainerListener {

  /*
  override val name: String
  override val pos: Pos
  override val level: Level
  override val displayName: Component get() = text(name)
  override val entity: Entity? get() = this
  override var sendFeedback: Boolean = true
   */
  
  /**
   * Gets the network of this player.
   */
  val network: NetworkManager get() = connection.network
  
  /**
   * Gets the network channel of this player.
   */
  val channel get() = network.channel
  
  /**
   * Gets the address of this player.
   */
  val address get() = network.address as InetSocketAddress
  
  /**
   * Gets the IP of this player.
   */
  val ip: String
    get() {
      var result = address.toString()
      result = result.substring(result.indexOf('/') + 1)
      result = result.substring(0, result.indexOf(':'))
      return result
    }
  
  /**
   * Gets all outbound packets that is been sending to this player.
   */
  val outboundPackets get() = network.outboundPackets
  
  /**
   * The inventory of this player.
   */
  var inventory = PlayerInventory(this)
  
  /**
   * The default container of this player.
   *
   * Opens this container when player press `E` keyword
   */
  var container: Container = PlayerContainer(this)
  
  /**
   * The opened container for this player.
   */
  var openContainer: Container = container
  
  /**
   * The current held slot of this player.
   */
  var heldSlot by inventory::heldSlot
  
  /**
   * The current held item of this player.
   */
  override var heldItem by inventory::heldItem
  
  /**
   * The current item on the cursor of this player.
   */
  var cursor by inventory::cursor
  
  /**
   * The food stats of this player.
   */
  var foodStats = FoodStats()
  
  /**
   * The nutrition level of this player.
   */
  var nutrition by foodStats::nutrition
  
  /**
   * The saturation level of this player.
   */
  var saturation by foodStats::saturation
  
  /**
   * The exhaustion level of this player.
   */
  var exhaustion by foodStats::exhaustion
  
  /**
   * Returns if this player needs food.
   */
  val needsFood: Boolean get() = foodStats.needsFood
  
  /**
   * Returns if this player is hunger.
   */
  val isHunger: Boolean get() = foodStats.isHunger
  
  /**
   * The current item that's this player is using.
   */
  var itemInUse: ItemStack? = null
  
  /**
   * Returns if this player is using any item.
   */
  val isUsingItem: Boolean get() = itemInUse != null
  
  /**
   * The current duration that's player is using [itemInUse].
   */
  var itemInUseDuration: Int = 0
  
  /**
   * Returns if damage should be disabled for this player.
   */
  var disableDamage = false
  
  /**
   * Returns if this player is currently flying.
   */
  var isFlying = false
  
  /**
   * Returns if this player is allowed to fly.
   */
  var allowFly = false
  
  /**
   * Indicates if items must be consumed on usage.
   */
  var consumeItems = false
  
  /**
   * Indicates whether the player is allowed to modify the surroundings.
   */
  var allowEdit = true
  
  /**
   * Returns the fly speed of this player.
   */
  var flySpeed = 0.05f
  
  /**
   * Returns the walk speed of this player.
   */
  var walkSpeed = 0.1f
  
  /**
   * The chat visibility configuration of the player.
   */
  var chatVisibility = ChatVisibility.FULL
  
  /**
   * The gamemode of this player.
   */
  var gamemode = Gamemode.SURVIVAL
    set(value) {
      if (field != value) {
        value.configure(this)
        field = value
      }
    }
  
  /**
   * Checks if the player is in survival mode.
   *
   * @return `true` if the player is in survival mode, `false` otherwise.
   */
  val isSurvival get() = gamemode.isSurvival
  
  /**
   * Checks if the player is in creative mode.
   *
   * @return `true` if the player is in creative mode, `false` otherwise.
   */
  val isCreative get() = gamemode.isCreative
  
  /**
   * Checks if the player is in adventure mode.
   *
   * @return `true` if the player is in adventure mode, `false` otherwise.
   */
  val isAdventure get() = gamemode.isAdventure
  
  /**
   * Checks if the player is in spectator mode.
   *
   * @return `true` if the player is in spectator mode, `false` otherwise.
   */
  val isSpectator get() = gamemode.isSpectator
  
  /**
   * Checks if the player is in survival or adventure mode.
   *
   * @return `true` if the player is in survival or adventure mode, `false` otherwise.
   */
  val isSurvivalOrAdventure get() = gamemode.isSurvivalOrAdventure
  
  /**
   * Checks if the player is in adventure or spectator mode.
   *
   * @return `true` if the player is in adventure or spectator mode, `false` otherwise.
   */
  val isAdventureOrSpectator get() = gamemode.isAdventureOrSpectator
  
  /**
   * If player is moving amount of items from one inventory to another
   * but item in either slot is not changed.
   */
  var isChangingAmountOnly = false
  
  /**
   * Gets if player is currently digging a block.
   */
  var isDigging: Boolean
    get() = interaction.isDigging
    set(value) {
      interaction.isDigging = value
    }
  
  /**
   * Gets the currently pos of the digging block.
   */
  var diggingPos: Pos
    get() = interaction.diggingPos
    set(value) {
      interaction.diggingPos = value
    }
  
  /**
   * Gets the currently digging block progress.
   */
  var digProgress: Int
    get() = interaction.remainingDigDurability
    set(value) {
      interaction.remainingDigDurability = value
    }
  
  /**
   * Tick this entity.
   */
  override fun tick(partial: Int) {
    interaction.tick(partial)
    super.tick(partial)
  }
  
  /**
   * Clears the held item of this player.
   */
  fun clearHeldItem() {
    heldItem = EmptyItemStack
  }
  
  fun clearHeldItemIfEmpty() {
    if (heldItem.isEmpty) {
      heldItem = EmptyItemStack
    }
  }
  
  /**
   * Decreases the held item of this player by [quantity].
   *
   * @return true if the held item amount is below or equal to 0 after decrease, false otherwise.
   */
  fun decreaseHeldItem(quantity: Int = 1): Boolean {
    return if (heldItem.shrinkIsEmpty(quantity)) {
      heldItem = EmptyItemStack
      true
    } else {
      false
    }
  }
  
  /**
   * Adds an [ItemStack] to the inventory or drops it at the specified [position].
   *
   * The [item] amount **will not be** subtracted.
   *
   * @param item the [ItemStack] to add or drop.
   * @param position the position where the item should be dropped (default: current position [pos]).
   * @return `true` if the item was successfully added to the inventory, or `false` if it had to be dropped.
   */
  fun addOrDrop(item: ItemStack, position: Pos = pos): Boolean {
    return if (!addItem(item)) {
      item.drop(level, position)
      false
    } else {
      true
    }
  }
  
  /**
   * Pickups an [ItemStack] to the inventory or drops it at the specified [position].
   *
   * The [item] amount **will be** subtracted.
   *
   * @param item the [ItemStack] to add or drop.
   * @param position the position where the item should be dropped (default: current position [pos]).
   * @return `true` if the item was successfully added to the inventory, or `false` if it had to be dropped.
   */
  fun pickupOrDrop(item: ItemStack, position: Pos = pos): Boolean {
    return if (!pickup(item)) {
      item.drop(level, position)
      false
    } else {
      true
    }
  }
  
  /**
   * Adds the specified ItemStack, merging if possible.
   *
   * The [item] amount **will not be** subtracted.
   *
   * @param item The ItemStack to add.
   * @return True if the stack was successfully added, false otherwise.
   */
  fun addItem(item: ItemStack) = inventory.add(item)
  
  /**
   * Picks up the specified ItemStack, merging if possible.
   *
   * The [item] amount **will be** subtracted.
   *
   * @param item The ItemStack to pick up.
   * @return True if the stack was successfully picked up, false otherwise.
   */
  fun pickup(item: ItemStack) = inventory.pickup(item)
  
  /**
   * Adds an ItemStack to the ItemList without merging.
   *
   * @param item The ItemStack to add.
   * @return `true` if the ItemStack was added successfully, `false` if there is no empty slot.
   */
  fun addWithoutMerge(item: ItemStack) = inventory.addWithoutMerge(item)
  
  /**
   * Removes the ItemStack from the specified slot.
   *
   * @param slot The slot index.
   * @return The removed ItemStack.
   */
  fun removeItem(slot: Int) = inventory.remove(slot)
  
  /**
   * Clears the inventory, removing all items.
   */
  fun clearInventory() = inventory.clear()
  
  /**
   * Checks if this player can harvest block with the current held item.
   */
  fun canHarvest(block: Block): Boolean {
    return !block.material.requiresTool || heldItem.canHarvest(block)
  }
  
  /**
   * Eats the given [food].
   */
  fun eat(food: Food) {
    foodStats.add(food)
  }
  
  /**
   * Returns if this player can eat.
   */
  fun canEat(ignoreHunger: Boolean = false): Boolean {
    return (ignoreHunger || foodStats.needsFood) && !disableDamage
  }
  
  /**
   * Sets the item in use of this player.
   */
  fun setItemInUse(item: ItemStack, duration: Int) {
    if (item == itemInUse) return
    
    itemInUse = item
    itemInUseDuration = duration
    isEating = true
  }
  
  /**
   * Stops using the current in use item.
   */
  fun stopUsingItem() {
    itemInUse?.onStoppedUsing(this, itemInUseDuration)
    clearItemInUse()
  }
  
  /**
   * Clears the item in use of this player.
   */
  fun clearItemInUse() {
    itemInUse = null
    itemInUseDuration = 0
    isEating = false
  }
  
  /**
   * Gets an item on the inventory of this player in the given [slot].
   */
  fun getItem(slot: Int): ItemStack {
    return inventory.getItem(slot)
  }
  
  /**
   * Sets an item on the inventory of this player in the given [slot].
   */
  fun setItem(slot: Int, item: ItemStack) {
    inventory.setItem(slot, item)
  }
  
  override fun update(container: Container, items: List<ItemStack>) {
    TODO("Not yet implemented")
  }
  
  override fun sendContent(container: Container, slot: Int, item: ItemStack) {
    TODO("Not yet implemented")
  }
  
  override fun sendData(container: Container, id: Int, value: Int) {
    TODO("Not yet implemented")
  }
  
  override fun sendProps(container: Container, inventory: IInventory) {
    TODO("Not yet implemented")
  }
  
  fun sendContainer(container: Container) {
    update(container, container.items)
  }
  
  /**
   * Sends [packet] to this player.
   */
  fun sendPacket(packet: HandledPacket) {
    connection.sendPacket(packet)
  }
  
  /**
   * Sends [packet] to this player.
   */
  fun sendPacket(packet: HandledPacket, vararg listeners: PacketListener) {
    connection.sendPacket(packet, *listeners)
  }
  
  /**
   * Sends [packet] to this player.
   */
  fun sendPacket(packet: HandledPacket, listener: PacketListener) {
    connection.sendPacket(packet, listener)
  }
  
  /**
   * Disconnects this player from the server.
   */
  fun disconnect(reason: String) = connection.disconnect(reason)
  
  /**
   * Plays [sound] with [volume] and [pitch] for this player.
   */
  fun playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
    sound.play(this, volume, pitch)
  }
  
  override fun toString(): String {
    return """
         Player $name ($id)
         IP $address
         ${if (hasLevel) "World ${level.name}" else "None World"}
         Position x: $x y: $y z: $z
      """.trimIndent()
  }
}
