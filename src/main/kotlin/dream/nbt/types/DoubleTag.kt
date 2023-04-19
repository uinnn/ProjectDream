package dream.nbt.types

import com.soywiz.kds.*
import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [Double].
 */
@Serializable(DoubleTagSerializer::class)
data class DoubleTag internal constructor(var value: Double = 0.0) :
   Number(),
   NumberTag,
   Comparable<DoubleTag>,
   java.io.Serializable {
   
   override val type
      get() = Type
   
   override val genericValue: Double
      get() = value
   
   override fun write(data: ObjectOutput) {
      data.writeDouble(value)
   }
   
   override fun toNumber(): Number = value
   override fun toByte(): Byte = toInt().toByte()
   override fun toChar(): Char = toInt().toChar()
   override fun toShort(): Short = toInt().toShort()
   override fun toInt(): Int = value.toInt()
   override fun toLong(): Long = value.toLong()
   override fun toFloat(): Float = value.toFloat()
   override fun toDouble(): Double = value
   
   override fun copy() = of(value)
   override fun toString() = "${value}D"
   override fun equals(other: Any?) = other is DoubleTag && value == other.value
   override fun hashCode() = hashCode(value)
   override fun compareTo(other: DoubleTag) = value.compareTo(other.value)
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      value = stream.readDouble()
   }
   
   private fun readObjectNoData() {
      value = 0.0
   }
   
   /**
    * Type tag of [DoubleTag].
    */
   object Type : TagType<DoubleTag>, TagParse<DoubleTag> {
      val REGEX = "[-+]?[0-9]*\\.?[0-9]+[d|D]".toRegex()
      val REGEX_UNTYPED = "[-+]?[0-9]*\\.?[0-9]+".toRegex()
      
      override val id: Byte get() = 6
      override val supportItems: Boolean get() = true
      
      override fun load(data: ObjectInput): DoubleTag {
         return of(data.readDouble())
      }
      
      override fun matches(str: String): Boolean {
         return REGEX.matches(str) || REGEX_UNTYPED.matches(str)
      }
      
      override fun parseOrNull(str: String): DoubleTag? {
         return when {
            REGEX.matches(str) -> of(str.dropLast(1).toDouble())
            REGEX_UNTYPED.matches(str) -> of(str.toDouble())
            else -> null
         }
      }
      
      override fun parse(str: String): DoubleTag {
         return parseOrNull(str) ?: ZERO
      }
   }
   
   companion object {
      @JvmField val ZERO = DoubleTag()
      
      fun of(value: Double): DoubleTag {
         return if (value == 0.0) ZERO else DoubleTag(value)
      }
   }
}

