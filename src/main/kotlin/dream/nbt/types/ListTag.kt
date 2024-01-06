package dream.nbt.types

import dream.nbt.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.Serializable
import java.io.*
import kotlin.contracts.*

/**
 * An implementation of [CollectionTag] as list.
 */
@Serializable(ListTagSerializer::class)
class ListTag<T : Tag> : CollectionTag<T>, java.io.Serializable {

  var elementType: TagType<out Tag>? = null

  override val type
    get() = Type

  constructor()

  constructor(values: Iterable<T>, type: TagType<out Tag>? = null) {
    addAll(values)
    elementType = type
  }

  constructor(values: Collection<T>, type: TagType<out Tag>? = null) : super(values) {
    elementType = type
  }

  override fun write(data: ObjectOutput) {
    val type = fetchType()
    data.writeByte(type.id.toInt())
    if (type !== EmptyType) {
      data.writeInt(size)
      for (element in this) {
        element.write(data)
      }
    }
  }
  
  override fun fetchType(): TagType<out Tag> {
    if (elementType == null) {
      elementType = firstOrNull()?.type ?: EmptyType
    }

    return elementType!!
  }

  override fun copy(): ListTag<T> = ListTag(this)

  override fun toString(): String = buildString {
    append('[')
    this@ListTag.forEachIndexed { index, tag ->
      if (index != 0) append(',')
      append(index).append(':').append(tag)
    }
    append(']')
  }

  private fun writeObject(stream: ObjectOutputStream) {
    write(stream)
  }

  private fun readObject(stream: ObjectInputStream) {
    val type = TagRegistry[stream.readByte()]
    val size = stream.readInt()
    repeat(size) {
      add(type.load(stream).cast())
    }
  }

  // nothing to do
  private fun readObjectNoData() {
  }

  /**
   * Tag type of [List].
   */
  object Type : TagType<ListTag<out Tag>> {
    override val id: Byte get() = 9
    override val supportItems: Boolean get() = true

    override fun load(data: ObjectInput): ListTag<out Tag> {
      val type = TagRegistry[data.readByte()]
      if (type === EmptyType)
        return EMPTY
      
      val size = data.readInt()
      val list = ListTag<Tag>()
      repeat(size) {
        list += type.load(data)
      }

      return list
    }
  }

  companion object {
    
    /**
     * Empty immutable list.
     *
     * TODO: make immutable.
     */
    val EMPTY = ListTag<Tag>()
    
    /**
     * Converts [data] in a NBT List.
     */
    fun <T : Tag> fromByteArray(data: ByteArray): ListTag<T> {
      return data.decodeTag().cast()
    }
  }
}

/**
 * Creates an empty [ListTag].
 */
fun <T : Tag> tagListOf() = ListTag<T>()

/**
 * Creates a [ListTag] with all [values].
 */
fun tagListOf(vararg values: Tag) = ListTag(values.toMutableList())

/**
 * Creates a [ListTag] with values of this list.
 */
fun Collection<Tag>.toTag() = ListTag(this)

/**
 * Creates a [ListTag] with values of this list.
 */
fun Iterable<Tag>.toTag() = ListTag(this)

/**
 * Populates a newly created [ListTag].
 */
inline fun <T : Tag> tagList(builder: ListTag<T>.() -> Unit): ListTag<T> {
  contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }
  return ListTag<T>().apply(builder)
}
