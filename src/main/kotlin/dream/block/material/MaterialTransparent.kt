package dream.block.material

import dream.item.*

/**
 * Represents a transparent material.
 */
public class MaterialTransparent(pallete: MapPallete) : Material(pallete, isReplaceable = true) {
   public override val isSolid: Boolean get() = false
   public override val blocksLight: Boolean get() = false
   public override val blocksMovement: Boolean get() = false
}
