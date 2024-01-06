package dream.block.material

import dream.item.MapPallete

/**
 * Special material for Web
 */
class MaterialWeb(pallete: MapPallete) : Material(pallete) {
  override val blocksMovement: Boolean get() = false
}
