package dream.block.material

import dream.block.*
import dream.item.*

/**
 * Represents a liquid material.
 */
public class MaterialLiquid(pallete: MapPallete) :
   Material(pallete, isReplaceable = true, mobility = PistonMobility.IMMOVABLE) {
   
   public override val isLiquid: Boolean get() = true
   public override val isSolid: Boolean get() = false
   public override val blocksMovement: Boolean get() = false
}
