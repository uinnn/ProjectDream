package dream.api

import dream.entity.player.*
import dream.inventory.*
import dream.inventory.container.*

/**
 * Represents an object that can create a container.
 */
interface Containerable : Nameable {
  
  /**
   * Represents the identifier of a container.
   */
  val containerId: String
  
  /**
   * Creates a container for the given player and inventory.
   *
   * @param player The player for whom the container is created.
   * @param inventory The player's inventory.
   * @return The created container.
   */
  fun createContainer(player: Player, inventory: PlayerInventory): Container
}
