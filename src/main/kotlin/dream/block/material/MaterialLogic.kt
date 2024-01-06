package dream.block.material

import dream.item.MapPallete

/**
 * Represents a logic material.
 */
class MaterialLogic(pallete: MapPallete) : Material(pallete, isAdventureModeExempt = true) {
  override val isSolid: Boolean get() = false
  override val blocksLight: Boolean get() = false
  override val blocksMovement: Boolean get() = false
}
