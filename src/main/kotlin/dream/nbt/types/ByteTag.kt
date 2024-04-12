package dream.nbt.types

import dream.nbt.NumberTag
import dream.nbt.TagParse
import dream.nbt.TagType
import dream.serializer.ByteTagSerializer
import korlibs.memory.toByte
import kotlinx.serialization.Serializable
import java.io.ObjectInput
import java.io.ObjectInputStream
import java.io.ObjectOutput
import java.io.ObjectOutputStream

/**
 * Represents an NBT tag of type [Byte].
 */
@Serializable(ByteTagSerializer::class)
data class ByteTag internal constructor(var value: Byte = 0) :
  Number(),
  NumberTag,
  Comparable<ByteTag>,
  java.io.Serializable {

  override val type
    get() = Type

  override val genericValue: Byte
    get() = value

  override fun write(data: ObjectOutput) {
    data.writeByte(toInt())
  }
  
  override fun changeValue(value: Any) {
    this.value = when (value) {
      is Number -> value.toByte()
      is Boolean -> value.toByte()
      else -> this.value
    }
  }

  override fun toNumber(): Number = value
  override fun toByte(): Byte = value
  override fun toShort(): Short = value.toShort()
  override fun toChar(): Char = toInt().toChar()
  override fun toInt(): Int = value.toInt()
  override fun toLong(): Long = value.toLong()
  override fun toFloat(): Float = value.toFloat()
  override fun toDouble(): Double = value.toDouble()

  override fun copy() = of(value)
  override fun toString() = "${value}b"
  override fun equals(other: Any?) = other is ByteTag && value == other.value
  override fun hashCode() = value.hashCode()
  override fun compareTo(other: ByteTag) = value.compareTo(other.value)

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    value = stream.readByte()
  }

  private fun readObjectNoData() {
    value = 0
  }

  /**
   * Type tag of [ByteTag].
   */
  object Type : TagType<ByteTag>, TagParse<ByteTag> {
    val REGEX = "[-+]?[0-9]+[b|B]".toRegex()

    override val id: Byte get() = 1
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): ByteTag {
      return of(data.readByte())
    }

    override fun matches(str: String): Boolean {
      return when {
        REGEX.matches(str) -> true
        str.equals("true", true) -> true
        str.equals("false", true) -> true
        else -> false
      }
    }

    override fun parseOrNull(str: String): ByteTag? {
      return when {
        REGEX.matches(str) -> of(str.dropLast(1).toByte())
        str.equals("true", true) -> ONE
        str.equals("false", true) -> ZERO
        else -> null
      }
    }

    override fun parse(str: String): ByteTag {
      return parseOrNull(str) ?: ZERO
    }
  }

  companion object {
    private val CACHE = Array(256) {
      ByteTag((it - 128).toByte())
    }

    @JvmField
    val ONE = of(1)
    @JvmField
    val ZERO = of(0)

    fun of(value: Byte) = CACHE[value + 128]
    fun of(value: Boolean) = if (value) ONE else ZERO
  }
}
