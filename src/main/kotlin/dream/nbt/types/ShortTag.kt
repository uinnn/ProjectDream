package dream.nbt.types

import com.soywiz.kds.*
import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [Short].
 */
@Serializable(ShortTagSerializer::class)
data class ShortTag internal constructor(var value: Short = 0) :
   Number(),
   NumberTag,
   Comparable<ShortTag>,
   java.io.Serializable {
   
   override val type
      get() = Type
   
   override val genericValue: Short
      get() = value
   
   override fun write(data: ObjectOutput) {
      data.writeShort(toInt())
   }
   
   override fun toNumber(): Number = value
   override fun toByte(): Byte = value.toByte()
   override fun toChar(): Char = toInt().toChar()
   override fun toShort(): Short = value
   override fun toInt(): Int = value.toInt()
   override fun toLong(): Long = value.toLong()
   override fun toFloat(): Float = value.toFloat()
   override fun toDouble(): Double = value.toDouble()
   
   override fun copy() = of(value)
   override fun toString() = "${value}s"
   override fun equals(other: Any?) = other is ShortTag && value == other.value
   override fun hashCode() = hashCode(value)
   override fun compareTo(other: ShortTag) = value.compareTo(other.value)
   
   private fun writeObject(stream: ObjectOutputStream) {
      write(stream)
   }
   
   private fun readObject(stream: ObjectInputStream) {
      value = stream.readShort()
   }
   
   private fun readObjectNoData() {
      value = 0
   }
   
   /**
    * Type tag of [ShortTag].
    */
   object Type : TagType<ShortTag>, TagParse<ShortTag> {
      val REGEX = "[-+]?[0-9]+[s|S]".toRegex()
      
      override val id: Byte get() = 2
      override val supportItems: Boolean get() = true
      
      override fun load(data: ObjectInput): ShortTag {
         return of(data.readShort())
      }
      
      override fun matches(str: String): Boolean {
         return REGEX.matches(str)
      }
      
      override fun parseOrNull(str: String): ShortTag? {
         return if (matches(str)) of(str.dropLast(1).toShort()) else null
      }
      
      override fun parse(str: String): ShortTag {
         return parseOrNull(str) ?: of(0)
      }
   }
   
   companion object {
      private val CACHE = Array(1153) {
         ShortTag((it - 128).toShort())
      }
      
      fun of(value: Short): ShortTag {
         return if (value in -128..1024) CACHE[value - -128] else ShortTag(value)
      }
   }
}

