package dream.app

import dream.block.*
import dream.block.state.*
import dream.entity.player.*
import dream.misc.*
import dream.pos.*
import io.netty.buffer.*
import it.unimi.dsi.fastutil.ints.*

fun main() {
  
  lateinit var player: Player
  val level = player.level
  val item = player.heldItem

}


/**
 *
 */
typealias IntCharMap = Int2CharOpenHashMap

/**
 * Abstract class representing positional storage for values of type [T].
 *
 * The [PosStorage] class provides a base implementation for storing and retrieving values based on their positions.
 * It uses a map to store the positions and their corresponding values.
 *
 * @param T The type of values stored in the positional storage. Must be a non-null type.
 * @param data The underlying map to store the positions and values. Defaults to an empty [IntCharMap] if not provided.
 */
@Open
abstract class PosStorage<T : Any>(val data: IntCharMap = IntCharMap()) {
  
  /**
   * Secondary constructor that initializes the positional storage from a [ByteBuf].
   *
   * @param buf The [ByteBuf] containing the data to read.
   * @param size The size of the positional storage. Defaults to the value read from the [buf].
   */
  constructor(buf: ByteBuf, size: Int = buf.readInt()) : this(IntCharMap(size)) {
    read(buf)
  }
  
  /**
   * Secondary constructor that initializes the positional storage from a byte array.
   *
   * @param array The byte array containing the data to read.
   */
  constructor(array: ByteArray) : this(Unpooled.wrappedBuffer(array))
  
  /**
   * Converts a value of type [T] to its corresponding ID (as a [Char]).
   *
   * @param value The value to convert.
   * @return The ID of the value.
   */
  abstract fun toId(value: T): Char
  
  /**
   * Converts an ID (as a [Char]) to its corresponding value of type [T].
   *
   * @param id The ID to convert.
   * @return The value corresponding to the ID.
   */
  abstract fun toValue(id: Char): T
  
  /**
   * Retrieves the ID at the specified position.
   *
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   * @param z The z-coordinate of the position.
   * @return The ID at the specified position.
   */
  fun getId(x: Int, y: Int, z: Int) = data.get(posToIndex(x, y, z))
  
  /**
   * Retrieves the ID at the specified position.
   *
   * @param pos The position.
   * @return The ID at the specified position.
   */
  fun getId(pos: Pos) = data.get(pos.toIndex())
  
  /**
   * Retrieves the value at the specified position.
   *
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   * @param z The z-coordinate of the position.
   * @return The value at the specified position.
   */
  fun getValue(x: Int, y: Int, z: Int) = toValue(getId(x, y, z))
  
  /**
   * Retrieves the value at the specified position.
   *
   * @param pos The position.
   * @return The value at the specified position.
   */
  fun getValue(pos: Pos) = toValue(getId(pos))
  
  /**
   * Sets the ID at the specified position.
   *
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   * @param z The z-coordinate of the position.
   * @param id The ID to set.
   */
  fun set(x: Int, y: Int, z: Int, id: Char) = data.put(posToIndex(x, y, z), id)
  
  /**
   * Sets the ID at the specified position.
   *
   * @param pos The position.
   * @param id The ID to set.
   */
  fun set(pos: Pos, id: Char) = data.put(pos.toIndex(), id)
  
  /**
   * Sets the value at the specified position.
   *
   * @param x The x-coordinate of the position.
   * @param y The y-coordinate of the position.
   * @param z The z-coordinate of the position.
   * @param value The value to set.
   */
  fun set(x: Int, y: Int, z: Int, value: T) = set(x, y, z, toId(value))
  
  /**
   * Sets the value at the specified position.
   *
   * @param pos The position.
   * @param value The value to set.
   */
  fun set(pos: Pos, value: T) = set(pos, toId(value))
  
  /**
   * Writes the positional storage data to a [ByteBuf].
   *
   * @param buf The [ByteBuf] to write the data to.
   */
  fun write(buf: ByteBuf) {
    buf.writeInt(data.size)
    data.int2CharEntrySet().fastForEach {
      buf.writeInt(it.intKey)
      buf.writeChar(it.charValue.code)
    }
  }
  
  /**
   * Reads the positional storage data from a [ByteBuf].
   *
   * @param buf The [ByteBuf] to read the data from.
   */
  fun read(buf: ByteBuf) {
    repeat(data.size) {
      data.put(buf.readInt(), buf.readChar())
    }
  }
  
  /**
   * Converts the positional storage data to a [ByteBuf].
   *
   * @return The [ByteBuf] containing the positional storage data.
   */
  fun toBuffer(): ByteBuf {
    val buf = Unpooled.buffer(data.size)
    write(buf)
    return buf
  }
  
  /**
   * Converts the positional storage data to a byte array.
   *
   * @return The byte array containing the positional storage data.
   */
  fun toBytes(): ByteArray = toBuffer().array()
}


class StateStorage : PosStorage<IState> {
  constructor(data: IntCharMap = IntCharMap()) : super(data)
  constructor(buf: ByteBuf) : super(buf)
  constructor(array: ByteArray) : super(array)
  
  override fun toId(value: IState): Char {
    return value.id.toChar()
  }
  
  override fun toValue(id: Char): IState {
    return Blocks.stateById(id.code)
  }
}
