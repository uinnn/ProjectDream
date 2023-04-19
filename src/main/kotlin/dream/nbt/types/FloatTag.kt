package dream.nbt.types

import com.soywiz.kds.*
import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [Float].
 */
@Serializable(FloatTagSerializer::class)
data class FloatTag internal constructor(var value: Float = 0f) :
   Number(),
   NumberTag,
   Comparable<FloatTag>,
   java.io.Serializable {
   
   override val type
      get() = Type
   
   override val genericValue: Float
      get() = value
   
   override fun write(data: ObjectOutput) {
      data.writeFloat(value)
   }
   
   override fun toNumber(): Number = value
   override fun toByte(): Byte = toInt().toByte()
   override fun toChar(): Char = toInt().toChar()
   override fun toShort(): Short = toInt().toShort()
   override fun toInt(): Int = value.toInt()
   override fun toLong(): Long = value.toLong()
   override fun toFloat(): Float = value
   override fun toDouble(): Double = value.toDouble()
   
   override fun copy() = of(value)
   override fun toString() = "${value}f"
   override fun equals(other: Any?) = other is FloatTag && value == other.value
   override fun hashCode() = hashCode(value)
   override fun compareTo(other: FloatTag) = value.compareTo(other.value)
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      value = stream.readFloat()
   }
   
   private fun readObjectNoData() {
      value = 0f
   }
   
   /**
    * Type tag of [FloatTag].
    */
   object Type : TagType<FloatTag>, TagParse<FloatTag> {
      val REGEX = "[-+]?[0-9]*\\.?[0-9]+[f|F]".toRegex()
      
      override val id: Byte get() = 5
      override val supportItems: Boolean get() = true
      
      override fun load(data: ObjectInput): FloatTag {
         return of(data.readFloat())
      }
      
      override fun matches(str: String): Boolean {
         return REGEX.matches(str)
      }
      
      override fun parseOrNull(str: String): FloatTag? {
         return if (matches(str)) of(str.dropLast(1).toFloat()) else null
      }
      
      override fun parse(str: String): FloatTag {
         return parseOrNull(str) ?: ZERO
      }
   }
   
   companion object {
      @JvmField val ZERO = FloatTag()
      
      fun of(value: Float): FloatTag {
         return if (value == 0f) ZERO else FloatTag(value)
      }
   }
}

