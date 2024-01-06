package dream.nbt.adapter

import dream.item.*
import dream.nbt.types.*
import dream.utils.*

/**
 * Collection of [ItemStack] tag adapter.
 */
object ItemListAdapter : TagAdapter<Iterable<ItemStack>> {
  override fun write(key: String, tag: CompoundTag, value: Iterable<ItemStack>) {
    tag[key] = value.elementsToTag()
  }

  override fun read(key: String, tag: CompoundTag, default: Iterable<ItemStack>?): Iterable<ItemStack> {
    val data = tag.listOrNull<CompoundTag>(key) ?: return default ?: return emptyList()
    return data.map(::ItemStack)
  }
}
