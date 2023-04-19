package dream.entity.player

import dream.*
import dream.entity.*
import dream.inventory.*
import dream.inventory.container.*
import dream.item.*
import dream.item.food.*
import dream.network.*
import dream.server.*
import dream.utils.*
import java.net.*

/**
 * Represents a player.
 */
abstract class Player(
   val profile: Profile,
   var server: Server,
   var connection: PlayerConnection,
   var interaction: PlayerInteraction,
) : EntityLiving() {
   
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
   var heldItem by inventory::heldItem
   
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
    * Used to determine if creative mode is enabled, and therefore if items should be depleted on usage.
    */
   var isCreative = false
   
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
      if (item == itemInUse)
         return
      
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
