package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

const val EMPTY_TAG_ID: Byte = 0

/**
 * Represents an empty tag.
 *
 * Empty tags don't write/read anything and is just for informational purposes.
 */
@Serializable(EmptyTagSerializer::class)
object EmptyTag : Tag, java.io.Serializable {
   
   override val type
      get() = Type
   
   override val genericValue: Any
      get() = Unit
   
   override fun write(data: ObjectOutput) = Unit
   override fun copy() = EmptyTag
   override fun toString() = "EmptyTag"
   
   private fun writeObject(stream: ObjectOutputStream) = Unit
   private fun readObject(stream: ObjectInputStream) = Unit
   private fun readObjectNoData() = Unit
   
   /**
    * Type tag of [EmptyTag].
    */
   object Type : TagType<EmptyTag>, TagParse<EmptyTag> {
      override val id: Byte get() = 0
      override val supportItems: Boolean get() = true
      
      override fun load(data: ObjectInput): EmptyTag {
         return EmptyTag
      }
      
      override fun matches(str: String): Boolean {
         return str.equals("EmptyTag", true)
      }
      
      override fun parseOrNull(str: String): EmptyTag {
         return EmptyTag
      }
      
      override fun parse(str: String): EmptyTag {
         return EmptyTag
      }
   }
}
