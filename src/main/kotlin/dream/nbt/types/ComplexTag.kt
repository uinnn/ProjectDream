package dream.nbt.types

import dream.nbt.*
import java.io.*

/**
 * Represents a [Serializable] type of tag.
 */
class ComplexTag(var value: Serializable) : Tag, Serializable {
   
   override val genericValue: Serializable
      get() = value
   
   override val type: TagType<out Tag>
      get() = Type
   
   override fun write(data: ObjectOutput) {
      data.writeObject(value)
   }
   
   override fun copy() = ComplexTag(value)
   override fun toString() = value.toString()
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      value = stream.readObject() as Serializable
   }
   
   private fun readObjectNoData() {
      value = SerializableUnit
   }
   
   object Type : TagType<ComplexTag> {
      override val id: Byte get() = 13
      override val supportItems: Boolean get() = false
   
      override fun load(data: ObjectInput): ComplexTag {
         return try {
            of(data.readObject())
         } catch (e: Exception) {
            e.printStackTrace()
            EMPTY
         }
      }
   }
   
   companion object {
      val EMPTY = ComplexTag(SerializableUnit)
      
      fun of(value: Any) = if (value is Serializable) ComplexTag(value) else EMPTY
   }
}

object SerializableUnit : Serializable
