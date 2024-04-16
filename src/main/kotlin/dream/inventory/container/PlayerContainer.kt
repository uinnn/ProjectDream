package dream.inventory.container

import dream.entity.player.Player
import dream.inventory.PlayerInventory

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
    return true
  }

}
