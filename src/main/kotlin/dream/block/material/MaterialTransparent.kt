package dream.block.material

import dream.item.*

/**
 * Represents a transparent material.
 */
class MaterialTransparent(pallete: MapPallete) : Material(pallete, isReplaceable = true) {
  override val isSolid: Boolean get() = false
  override val blocksLight: Boolean get() = false
  override val blocksMovement: Boolean get() = false
}
