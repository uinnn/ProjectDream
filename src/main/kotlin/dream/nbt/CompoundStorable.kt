package dream.nbt

import dream.nbt.types.*

/**
 * Represents an object that's can be storable in NBT form.
 */
interface Storable<T : Tag> {

  /**
   * Saves this storable object in [tag].
   */
  fun save(tag: T)

  /**
   * Loads this storable object from [tag].
   */
  fun load(tag: T)

  /**
   * Creates a new [T] with all data of this storable object.
   */
  fun store(): T

  /**
   * Encodes this storable object to a ByteArray.
   */
  fun toBytes() = store().toBytes()
}

/**
 * Represents an object that's can be storable in Compound NBT form.
 */
interface CompoundStorable : Storable<CompoundTag> {

  /**
   * Creates a new [CompoundTag] with all data of this storable object.
   */
  override fun store(): CompoundTag {
    val tag = CompoundTag()
    save(tag)
    return tag
  }
}

/**
 * Represents an object that's can be storable in Compound NBT form.
 */
interface ListStorable<T : Tag> : Storable<ListTag<T>> {

  /**
   * Creates a new [ListTag] with all data of this storable object.
   */
  override fun store(): ListTag<T> {
    val tag = ListTag<T>()
    save(tag)
    return tag
  }
}

/**
 * Represents an object that's can be storable in IntArrayTag form.
 */
interface IntArrayStorable : Storable<IntArrayTag> {

  /**
   * Creates a new [IntArrayTag] with all data of this storable object.
   */
  override fun store(): IntArrayTag {
    val tag = IntArrayTag()
    save(tag)
    return tag
  }
}

/**
 * Represents an object that's can be storable in LongArrayTag form.
 */
interface LongArrayStorable : Storable<LongArrayTag> {

  /**
   * Creates a new [LongArrayTag] with all data of this storable object.
   */
  override fun store(): LongArrayTag {
    val tag = LongArrayTag()
    save(tag)
    return tag
  }
}
