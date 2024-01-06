package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [Int].
 */
@Serializable(IntTagSerializer::class)
data class IntTag internal constructor(var value: Int = 0) :
  Number(),
  NumberTag,
  Comparable<IntTag>,
  java.io.Serializable {

  override val type
    get() = Type

  override val genericValue: Int
    get() = value

  override fun write(data: ObjectOutput) {
    data.writeInt(value)
  }

  override fun changeValue(value: Any) {
    if (value is Number) {
      this.value = value.toInt()
    }
  }
  
  override fun toNumber(): Number = value
  override fun toByte(): Byte = value.toByte()
  override fun toChar(): Char = value.toChar()
  override fun toShort(): Short = value.toShort()
  override fun toInt(): Int = value
  override fun toLong(): Long = value.toLong()
  override fun toFloat(): Float = value.toFloat()
  override fun toDouble(): Double = value.toDouble()

  override fun copy() = of(value)
  override fun toString() = value.toString()
  override fun equals(other: Any?) = other is IntTag && value == other.value
  override fun hashCode() = value.hashCode()
  override fun compareTo(other: IntTag) = value.compareTo(other.value)

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    value = stream.readInt()
  }

  private fun readObjectNoData() {
    value = 0
  }

  /**
   * Type tag of [IntTag].
   */
  object Type : TagType<IntTag>, TagParse<IntTag> {
    val REGEX = "[-+]?[0-9]+".toRegex()

    override val id: Byte get() = 3
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): IntTag {
      return of(data.readInt())
    }

    override fun matches(str: String): Boolean {
      return REGEX.matches(str)
    }

    override fun parseOrNull(str: String): IntTag? {
      return if (matches(str)) of(str.toInt()) else null
    }

    override fun parse(str: String): IntTag {
      return parseOrNull(str) ?: of(0)
    }
  }

  companion object {
    private val CACHE = Array(1153) {
      IntTag(it - 128)
    }

    fun of(value: Int): IntTag {
      return if (value in -128..1024) CACHE[value - -128] else IntTag(value)
    }
  }
}

