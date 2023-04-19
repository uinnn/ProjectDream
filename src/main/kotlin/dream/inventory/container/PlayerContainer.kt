package dream.inventory.container

import dream.entity.player.*
import dream.inventory.*

/**
 * Represents a container for players.
 */
class PlayerContainer(val player: Player) : Container() {
   
   /**
    * Gets the player inventory.
    */
   val inventory: PlayerInventory
      get() = player.inventory
   
   override fun canInteract(player: Player): Boolean {
      TODO("Not yet implemented")
   }
   
   
}
