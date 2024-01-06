package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [Long].
 */
@Serializable(LongTagSerializer::class)
data class LongTag internal constructor(var value: Long = 0) :
  Number(),
  NumberTag,
  Comparable<LongTag>,
  java.io.Serializable {

  override val type
    get() = Type

  override val genericValue: Long
    get() = value

  override fun changeValue(value: Any) {
    if (value is Number) {
      this.value = value.toLong()
    }
  }
  
  override fun toNumber(): Number = value
  override fun toByte(): Byte = value.toByte()
  override fun toChar(): Char = toInt().toChar()
  override fun toShort(): Short = value.toShort()
  override fun toInt(): Int = value.toInt()
  override fun toLong(): Long = value
  override fun toFloat(): Float = value.toFloat()
  override fun toDouble(): Double = value.toDouble()

  override fun write(data: ObjectOutput) {
    data.writeLong(value)
  }

  override fun copy() = of(value)
  override fun toString() = "${value}L"
  override fun equals(other: Any?) = other is LongTag && value == other.value
  override fun hashCode() = value.hashCode()
  override fun compareTo(other: LongTag) = value.compareTo(other.value)

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    value = stream.readLong()
  }

  private fun readObjectNoData() {
    value = 0
  }

  /**
   * Type tag of [LongTag].
   */
  object Type : TagType<LongTag>, TagParse<LongTag> {
    val REGEX = "[-+]?[0-9]+[l|L]".toRegex()

    override val id: Byte get() = 4
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): LongTag {
      return of(data.readLong())
    }

    override fun matches(str: String): Boolean {
      return REGEX.matches(str)
    }

    override fun parseOrNull(str: String): LongTag? {
      return if (matches(str)) of(str.dropLast(1).toLong()) else null
    }

    override fun parse(str: String): LongTag {
      return parseOrNull(str) ?: of(0)
    }
  }

  companion object {
    private val CACHE = Array(1153) {
      LongTag((it - 128).toLong())
    }

    fun of(value: Long): LongTag {
      return if (value in -128..1024) CACHE[(value - -128).toInt()] else LongTag(value)
    }
  }
}
