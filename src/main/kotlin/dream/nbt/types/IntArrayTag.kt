package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.Serializable
import java.io.*

/**
 * Represents an NBT tag of type [IntArrayTag].
 */
@Serializable(IntArrayTagSerializer::class)
class IntArrayTag : IntList, ArrayTag, java.io.Serializable {

  override val type
    get() = Type

  override val genericValue: IntArray
    get() = a

  val value: IntArray
    get() = a

  constructor() : super()
  constructor(size: Int) : super(size)
  constructor(value: IntArray) : super(value)
  //constructor(vararg elements: Int) : super(elements)

  override fun write(data: ObjectOutput) {
    data.writeInt(size)

    // use unboxed int values
    with(intIterator()) {
      while (hasNext()) data.writeInt(nextInt())
    }
  }
  
  override fun changeValue(value: Any) {
    if (value is IntArray) {
      a = value
    }
  }

  override fun elementType() = IntType
  override fun copy() = IntArrayTag(value)

  override fun toString(): String = buildString {
    append('[')
    this@IntArrayTag.forEach {
      append("$it,")
    }
    append(']')
  }

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    a = IntArray(stream.readInt()) { stream.readInt() }
  }

  private fun readObjectNoData() {
    a = IntArray(0)
  }

  /**
   * Type tag of [IntArrayTag].
   */
  object Type : TagType<IntArrayTag> {
    override val id: Byte get() = 11
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): IntArrayTag {
      val size = data.readInt()
      val array = IntArray(size) {
        data.readInt()
      }

      return IntArrayTag(array)
    }
  }
}

fun intArrayTagOf(vararg elements: Int) = IntArrayTag(elements)
