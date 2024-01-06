package dream.block.material

import dream.item.*

/**
 * Represents a portal material.
 */
class MaterialPortal(pallete: MapPallete) : Material(pallete) {
  override val isSolid: Boolean get() = false
  override val blocksLight: Boolean get() = false
  override val blocksMovement: Boolean get() = false
}
