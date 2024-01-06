package dream.block.material

import dream.block.*
import dream.item.*
import dream.misc.*

/**
 * Represents a material for a block.
 *
 * This allow for specialized action by a specific material block.
 */
@Open
data class Material(
  var pallete: MapPallete,
  var canBurn: Boolean = false,
  var isReplaceable: Boolean = false,
  var isTranslucent: Boolean = false,
  var requiresTool: Boolean = false,
  var mobility: PistonMobility = PistonMobility.FREE,
  var isAdventureModeExempt: Boolean = false,
) {

  /**
   * Determinates if this material is a liquid.
   */
  val isLiquid: Boolean get() = false

  /**
   * Determinates if this material is solid.
   */
  val isSolid: Boolean get() = true

  /**
   * Determinates if this material should blocks light for passing.
   */
  val blocksLight: Boolean get() = true

  /**
   * Determinates if this material should blocks movement.
   */
  val blocksMovement: Boolean get() = true

  /**
   * Determinates if this material is opaque.
   */
  val isOpaque: Boolean get() = if (isTranslucent) false else blocksMovement

  fun replaceable(): Material {
    isReplaceable = true
    return this
  }

  fun requiresTool(): Material {
    requiresTool = true
    return this
  }

  fun translucent(): Material {
    isTranslucent = true
    return this
  }

  fun burnable(): Material {
    canBurn = true
    return this
  }

  fun immovable(): Material {
    mobility = PistonMobility.IMMOVABLE
    return this
  }

  fun unpushable(): Material {
    mobility = PistonMobility.UNPUSHABLE
    return this
  }

  fun adventureExempt(): Material {
    isAdventureModeExempt = true
    return this
  }
}
