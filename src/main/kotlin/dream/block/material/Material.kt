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
public data class Material(
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
   public val isLiquid: Boolean get() = false
   
   /**
    * Determinates if this material is solid.
    */
   public val isSolid: Boolean get() = true
   
   /**
    * Determinates if this material should blocks light for passing.
    */
   public val blocksLight: Boolean get() = true
   
   /**
    * Determinates if this material should blocks movement.
    */
   public val blocksMovement: Boolean get() = true
   
   /**
    * Determinates if this material is opaque.
    */
   public val isOpaque: Boolean get() = if (isTranslucent) false else blocksMovement
   
   public fun replaceable(): Material {
      isReplaceable = true
      return this
   }
   
   public fun requiresTool(): Material {
      requiresTool = true
      return this
   }
   
   public fun translucent(): Material {
      isTranslucent = true
      return this
   }
   
   public fun burnable(): Material {
      canBurn = true
      return this
   }
   
   public fun immovable(): Material {
      mobility = PistonMobility.IMMOVABLE
      return this
   }
   
   public fun unpushable(): Material {
      mobility = PistonMobility.UNPUSHABLE
      return this
   }
   
   public fun adventureExempt(): Material {
      isAdventureModeExempt = true
      return this
   }
}
