package dream.block.material

import dream.item.*

/**
 * Special material for Web
 */
public class MaterialWeb(pallete: MapPallete) : Material(pallete) {
   override val blocksMovement: Boolean get() = false
}
