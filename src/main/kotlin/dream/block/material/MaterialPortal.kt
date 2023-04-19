package dream.block.material

import dream.item.*

/**
 * Represents a portal material.
 */
public class MaterialPortal(pallete: MapPallete) : Material(pallete) {
   public override val isSolid: Boolean get() = false
   public override val blocksLight: Boolean get() = false
   public override val blocksMovement: Boolean get() = false
}
