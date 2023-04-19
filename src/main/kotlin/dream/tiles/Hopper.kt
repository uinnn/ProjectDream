package dream.tiles

import dream.inventory.*
import dream.level.*

/**
 * Represents a base interface for hopper.
 */
interface Hopper : IInventory {
   val level: Level
   val x: Double
   val y: Double
   val z: Double
}
