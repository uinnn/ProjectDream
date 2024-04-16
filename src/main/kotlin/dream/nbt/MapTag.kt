package dream.nbt

import dream.utils.*
import java.io.*

/**
 * An abstract implementation of [Tag] that's supports a map of elements.
 */
abstract class MapTag<K, V : Tag> : HashMap<K, V>, Tag, Serializable {

  override val genericValue: HashMap<K, V>
    get() = this

  constructor() : super(8, 0.8f)
  constructor(size: Int) : super(size)
  constructor(values: Map<K, V>) : super(values)
  constructor(vararg values: Pair<K, V>) : super(values.toMap())

  /**
   * Writes the given entry on [data].
   */
  abstract fun writeEntry(key: K, value: V, data: ObjectOutput)

  /**
   * Reads a entry and put them on this map.
   */
  fun readEntry(type: TagType<out V>, key: K, data: ObjectInput): V {
    val value = type.load(data)
    put(key, value)
    return value
  }

  override fun write(data: ObjectOutput) {
    for ((key, value) in this) {
      writeEntry(key, value, data)
    }
    data.writeByte(0) // end
  }
  
  override fun changeValue(value: Any) {
    if (value is Map<*, *>) {
      clear()
      putAll(value.cast())
    }
  }
  
  override fun toString(): String {
    return genericValue.toString()
  }
  
  override fun equals(other: Any?): Boolean {
    if (other !is MapTag<*, *>)
      return false
    
    return genericValue == other.genericValue
  }
  
  override fun hashCode(): Int {
    return genericValue.hashCode()
  }
}
