package dream.nbt.types

import dream.nbt.*
import dream.utils.*
import java.io.*

/**
 * Represents a NBT tag of type [LongArray].
 */
public class LongArrayTag : LongList, ArrayTag, Serializable {
   
   override val type
      get() = Type
   
   override val genericValue: LongArray
      get() = a
   
   val value: LongArray
      get() = a
   
   constructor() : super()
   constructor(size: Int) : super(size)
   constructor(value: LongArray) : super(value)
   
   override fun write(data: ObjectOutput) {
      data.writeInt(size)
      
      // use unboxed long values
      with(longIterator()) {
         while (hasNext()) data.writeLong(nextLong())
      }
   }
   
   override fun elementType() = LongType
   override fun copy() = LongArrayTag(value)
   
   override fun toString(): String = buildString {
      append('[')
      this@LongArrayTag.forEach {
         append("$it,")
      }
      append(']')
   }
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      a = LongArray(stream.readInt()) { stream.readLong() }
   }
   
   private fun readObjectNoData() {
      a = LongArray(0)
   }
   
   /**
    * Type tag of [IntArrayTag].
    */
   object Type : TagType<LongArrayTag> {
      override val id: Byte get() = 12
      override val supportItems: Boolean get() = false
      
      override fun load(data: ObjectInput): LongArrayTag {
         val size = data.readInt()
         val array = LongArray(size) {
            data.readLong()
         }
         
         return LongArrayTag(array)
      }
   }
   
}
