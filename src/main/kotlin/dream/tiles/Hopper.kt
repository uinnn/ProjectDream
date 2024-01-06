package dream.tiles

import dream.inventory.IInventory
import dream.level.Level

/**
 * Represents a base interface for hopper.
 */
interface Hopper : IInventory {
  val level: Level
  val x: Double
  val y: Double
  val z: Double
}
