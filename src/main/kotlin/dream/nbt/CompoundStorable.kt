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
   fun toTag(): T
   
   /**
    * Encodes this storable object to a ByteArray.
    */
   fun toByteArray() = toTag().toBytes()
}

/**
 * Represents an object that's can be storable in Compound NBT form.
 */
interface CompoundStorable : Storable<CompoundTag> {
   
   /**
    * Creates a new [CompoundTag] with all data of this storable object.
    */
   override fun toTag(): CompoundTag {
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
   override fun toTag(): ListTag<T> {
      val tag = ListTag<T>()
      save(tag)
      return tag
   }
}

