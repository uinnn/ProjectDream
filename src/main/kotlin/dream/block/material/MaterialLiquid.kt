package dream.block.material

import dream.block.*
import dream.item.*

/**
 * Represents a liquid material.
 */
class MaterialLiquid(pallete: MapPallete) :
  Material(pallete, isReplaceable = true, mobility = PistonMobility.IMMOVABLE) {

  override val isLiquid: Boolean get() = true
  override val isSolid: Boolean get() = false
  override val blocksMovement: Boolean get() = false
}
