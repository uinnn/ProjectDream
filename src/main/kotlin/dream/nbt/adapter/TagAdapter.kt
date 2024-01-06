package dream.nbt.adapter

import dream.nbt.types.*

/**
 * An adapter for NBT.
 *
 * This can be used for creating custom serializer for [T] type in NBT.
 */
interface TagAdapter<T : Any> {

  /**
   * Writes the adapted value in [tag] with the given [key].
   */
  fun write(key: String, tag: CompoundTag, value: T)

  /**
   * Reads the adapted value in [tag] with the given [key].
   *
   * @param default an optional default value defined if [key] is not present in [tag].
   */
  fun read(key: String, tag: CompoundTag, default: T?): T
}
