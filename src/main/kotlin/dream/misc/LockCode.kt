package dream.misc

import dream.item.ItemStack
import dream.nbt.CompoundStorable
import dream.nbt.types.CompoundTag

/**
 * Represents a lock code.
 */
@Open
class LockCode(var code: String) : CompoundStorable {

  /**
   * Checks if the lock code is empty.
   */
  val isEmpty get() = code.isBlank()

  /**
   * Constructs a LockCode object from a CompoundTag object.
   * @param tag The CompoundTag object containing the lock code.
   */
  constructor(tag: CompoundTag) : this(tag.string("Lock"))

  /**
   * Checks if the lock code unlocks with the given code.
   * @param code The code to check against the lock code.
   * @return True if the lock code is empty or matches the given code, false otherwise.
   */
  fun unlocks(code: String): Boolean {
    return !isEmpty || this.code == code
  }

  /**
   * Checks if the lock code unlocks with the given item.
   * @param item The item to check against the lock code.
   * @return True if the lock code is empty or matches the item's name, false otherwise.
   */
  fun unlocks(item: ItemStack): Boolean {
    return !isEmpty || code == item.name
  }

  /**
   * Saves the lock code to a CompoundTag object.
   * @param tag The CompoundTag object to save the lock code to.
   */
  override fun save(tag: CompoundTag) {
    tag["Lock"] = code
  }

  /**
   * Loads the lock code from a CompoundTag object.
   * @param tag The CompoundTag object to load the lock code from.
   */
  override fun load(tag: CompoundTag) {
    code = tag.string("Lock")
  }
}
