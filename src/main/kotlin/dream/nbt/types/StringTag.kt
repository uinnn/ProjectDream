package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [String].
 */
@Serializable(StringTagSerializer::class)
data class StringTag internal constructor(var value: String = "") :
  Tag,
  CharSequence,
  Comparable<StringTag>,
  java.io.Serializable {

  override val type
    get() = Type

  override val length: Int
    get() = value.length

  override val genericValue: String
    get() = value

  override fun write(data: ObjectOutput) {
    data.writeUTF(value)
  }
  
  override fun changeValue(value: Any) {
    this.value = value.toString()
  }

  override fun get(index: Int): Char = value[index]
  override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

  override fun copy() = of(value)
  override fun toString() = "\"" + value.replace("\"", "\\\"") + "\""
  override fun equals(other: Any?) = other is StringTag && value == other.value
  override fun hashCode() = value.hashCode()
  override fun compareTo(other: StringTag) = value.compareTo(other.value)

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    value = stream.readUTF()
  }

  private fun readObjectNoData() {
    value = ""
  }

  /**
   * Type tag of [StringTag].
   */
  object Type : TagType<StringTag> {
    override val id: Byte get() = 8
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): StringTag {
      return of(data.readUTF())
    }

    fun parse(str: String): StringTag {
      return of(str)
    }
  }

  companion object {
    @JvmField
    val EMPTY = StringTag()

    fun of(value: String): StringTag {
      return if (value.isEmpty()) EMPTY else StringTag(value)
    }
  }
}
