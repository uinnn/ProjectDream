package dream.pos.map

import dream.misc.Open
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

@Open
abstract class PosMap<T : Any> {

  /**
   * Calculates the EXACT amount of bytes that's this map will occup.
   */
  abstract val bytes: Int

  /**
   * Converts a value of type [T] to its corresponding ID (as a [Char]).
   *
   * @param value The value to convert.
   * @return The ID of the value.
   */
  abstract fun toId(value: T): Byte

  /**
   * Converts an ID (as a [Char]) to its corresponding value of type [T].
   *
   * @param id The ID to convert.
   * @return The value corresponding to the ID.
   */
  abstract fun toValue(id: Byte): T

  /**
   * Retrieves the ID at the specified position.
   */
  abstract fun getId(x: Int, y: Int, z: Int): Byte

  /**
   * Retrieves the value at the specified position.
   */
  fun getValue(x: Int, y: Int, z: Int) = toValue(getId(x, y, z))

  /**
   * Sets the ID at the specified position.
   */
  abstract fun set(x: Int, y: Int, z: Int, id: Byte)

  /**
   * Sets the value at the specified position.
   */
  fun set(x: Int, y: Int, z: Int, value: T) = set(x, y, z, toId(value))

  /**
   * Checks if the positional storage contains the specified position.
   */
  abstract fun contains(x: Int, y: Int, z: Int): Boolean

  /**
   * Removes the value at the specified position.
   */
  abstract fun remove(x: Int, y: Int, z: Int)

  /**
   * Writes the positional storage data to a [ByteBuf].
   *
   * @param buf The [ByteBuf] to write the data to.
   */
  abstract fun write(buf: ByteBuf)

  /**
   * Reads the positional storage data from a [ByteBuf].
   *
   * @param buf The [ByteBuf] to read the data from.
   */
  abstract fun read(buf: ByteBuf, size: Int = buf.readInt())

  /**
   * Converts the positional storage data to a [ByteBuf].
   *
   * @return The [ByteBuf] containing the positional storage data.
   */
  fun toBuffer(): ByteBuf {
    val buf = Unpooled.buffer(bytes)
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
