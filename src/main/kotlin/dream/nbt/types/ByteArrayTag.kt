package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [ByteArray].
 */
@Serializable(ByteArrayTagSerializer::class)
class ByteArrayTag : ByteList, ArrayTag, java.io.Serializable {

  override val type
    get() = Type

  override val genericValue: ByteArray
    get() = a

  val value: ByteArray
    get() = a

  constructor() : super()
  constructor(size: Int) : super(size)
  constructor(value: ByteArray) : super(value)
  //constructor(vararg elements: Byte) : super(elements)

  override fun write(data: ObjectOutput) {
    data.writeInt(size)
    data.write(value)
  }
  
  override fun changeValue(value: Any) {
    if (value is ByteArray) {
      a = value
    }
  }

  override fun elementType() = ByteType
  override fun copy() = ByteArrayTag(value)

  override fun toString(): String = buildString {
    append('[')
    this@ByteArrayTag.forEach {
      append("$it,")
    }
    append(']')
  }

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    val array = ByteArray(stream.readInt())
    stream.readFully(array)
    a = array
  }

  private fun readObjectNoData() {
    a = ByteArray(0)
  }

  /**
   * Type tag of [ByteArrayTag].
   */
  object Type : TagType<ByteArrayTag> {
    override val id: Byte get() = 7
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): ByteArrayTag {
      val array = ByteArray(data.readInt())
      data.readFully(array)
      return ByteArrayTag(array)
    }
  }
}

fun byteArrayTagOf(vararg elements: Byte) = ByteArrayTag(elements)
