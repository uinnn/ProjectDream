package dream.block.material

import dream.item.*

/**
 * Represents a logic material.
 */
public class MaterialLogic(pallete: MapPallete) : Material(pallete, isAdventureModeExempt = true) {
   public override val isSolid: Boolean get() = false
   public override val blocksLight: Boolean get() = false
   public override val blocksMovement: Boolean get() = false
}
