package dream.item.block

import dream.block.Block
import dream.item.*

/**
 * Represents a item block.
 */
class ItemBlock(override var block: Block) : Item() {

  override fun getUnlocalizedName(): String {
    return block.unlocalName
  }

  override fun getUnlocalizedName(item: ItemStack): String {
    return block.unlocalName
  }
}
