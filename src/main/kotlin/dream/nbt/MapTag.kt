package dream.nbt

import java.io.*

/**
 * An abstract implementation of [Tag] that's supports a map of elements.
 */
abstract class MapTag<K, V : Tag> : HashMap<K, V>, Tag, Serializable {
   
   override val genericValue: HashMap<K, V>
      get() = this
   
   constructor() : super()
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
      for (entry in this) {
         writeEntry(entry.key, entry.value, data)
      }
      data.writeByte(0) // end
   }
}
