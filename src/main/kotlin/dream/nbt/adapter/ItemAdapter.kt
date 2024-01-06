package dream.nbt.adapter

import dream.item.*
import dream.nbt.types.*

/**
 * [ItemStack] tag adapter.
 */
object ItemAdapter : TagAdapter<ItemStack> {
  override fun write(key: String, tag: CompoundTag, value: ItemStack) {
    tag[key] = value.store()
  }

  override fun read(key: String, tag: CompoundTag, default: ItemStack?): ItemStack {
    val data = tag.compoundOrNull(key) ?: return default ?: return EmptyItemStack
    return ItemStack(data)
  }
}
