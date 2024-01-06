package dream.item

import dream.block.Block
import dream.misc.Open
import dream.nbt.types.CompoundTag

/**
 * Represents a pair of [Block] and [Boolean] to determinate the
 * cached results of adventure mode place/break data using [ItemStack].
 */
@Open
class AdventureCheck(var name: String, var block: Block? = null, var result: Boolean = false) {

  /**
   * Verify and updates the cached results of the adventure checker.
   */
  fun verify(tag: CompoundTag, verifier: Block): Boolean {
    if (verifier == block) {
      return result
    }

    block = verifier

    if (name in tag) {
      for (value in tag.stringList(name)) {
        val new = Block() // TODO: 23/05/2022 CERTAINLY OBTAIN BLOCK BY ID
        if (new == verifier) {
          result = true
          return true
        }
      }
    }

    result = false
    return false
  }

  /**
   * Verify and updates the cached results of the adventure checker.
   */
  fun verify(item: ItemStack, verifier: Block): Boolean = verify(item.tag, verifier)
}
