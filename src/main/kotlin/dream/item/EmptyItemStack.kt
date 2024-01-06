package dream.item

import dream.nbt.types.*

/**
 * Empty [ItemStack].
 *
 * Should be used instead null.
 *
 * This not supports changing their tag.
 */
object EmptyItemStack : ItemStack(Items.AIR) {
  
  override var item: Item get() = Items.AIR; set(value) {}
  override var tag: CompoundTag get() = ImmutableGlobalCompound; set(value) {}
  override var amount: Int get() = 0; set(value) {}
  override var metadata: Int get() = 0; set(value) {}
  override var ticks: Int get() = super.ticks; set(value) {}
  override var popTime: Int get() = super.popTime; set(value) {}
  
  override val isAir: Boolean get() = true
  
  override fun copy(): ItemStack = this
  override fun copy(amount: Int): ItemStack = this
  override fun copyOrNull(): ItemStack? = null
  override fun copyOrNull(amount: Int): ItemStack? = null
  override fun orNull(): ItemStack? = null
  override fun hashCode(): Int = 0
  
  override fun equals(other: Any?): Boolean = other === this
  override fun isSimilar(other: ItemStack, checkTag: Boolean): Boolean = other === this
  override fun isEquals(other: ItemStack, checkTag: Boolean, checkAmount: Boolean): Boolean = other === this
  override fun toString(): String = "EmptyItemStack()"
  
  override fun save(tag: CompoundTag) {}
  override fun load(tag: CompoundTag) {}
}
