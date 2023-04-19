package dream.interfaces

import dream.entity.player.*
import dream.inventory.*
import dream.inventory.container.*

/**
 * Represents a object that can create a container.
 */
public interface Containerable : Nameable {
   public val containerId: String
   
   public fun createContainer(player: Player, inventory: PlayerInventory): Container
}
