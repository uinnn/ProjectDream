package dream.pos.map

import dream.misc.Open
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ByteOpenHashMap
import korlibs.memory.insert
import korlibs.memory.insert8

typealias IntByteMap = Int2ByteOpenHashMap

/**
 * Position Map is a lightweight map-like data structure with **hash and bits** that can be used
 * to store and retrieve position based data as key and id-like data as value.
 *
 * This is faster and lower memory footprint than a convencional [HashMap].
 *
 * Also this is inspired in the way Minecraft stores blocks in the chunks
 * which is why it is called Position Map.
 *
 * Although this map is not made for use to store complex data structures, this is useful
 * when you need to store a lot of position based data and a lot of id based data.
 *
 * The main purpose of this class is to store data and retrieve it in a
 * fast and efficient way (better than a [HashMap]) with lower memory footprint
 * and provides very compact serialization/deserialization, as this just stores primitive types.
 *
 * This implementation can store a max of:
 *
 * key x-pos: *4.096* (12 bits)
 *
 * key y-pos: *256* (8 bits)
 *
 * key z-pos: *4.096* (12 bits)
 *
 * value: *256* (8 bits)
 */
@Open
abstract class SmallPosMap<T : Any> : PosMap<T> {
  companion object {
    const val MAX_X = 4096
    const val MAX_Y = 256
    const val MAX_Z = 4096
    const val BYTES_PER_ENTRY = 5 // (int key) 4 + 1 (byte value)
  }
  
  /**
   * The data used to store.
   */
  lateinit var data: IntByteMap
  
  /**
   * Calculates the EXACT amount of bytes that's this map will occup.
   */
  override val bytes: Int get() = 4 + data.size * BYTES_PER_ENTRY

  constructor() : this(IntByteMap(8, 0.8F))

  constructor(data: IntByteMap) {
    this.data = data
  }

  constructor(buf: ByteBuf) {
    val size = buf.readInt()
    data = IntByteMap(size, 0.8F)
    read(buf, size)
  }

  init {
    data.defaultReturnValue(-1)
  }
  
  /**
   * Secondary constructor that initializes the positional storage from a byte array.
   *
   * @param array The byte array containing the data to read.
   */
  constructor(array: ByteArray) : this(Unpooled.wrappedBuffer(array))
  
  /**
   * Converts the given positions as a unique key.
   */
  fun toKey(x: Int, y: Int, z: Int): Int {
    return 0.insert(x, 0, 12).insert8(y, 12).insert(z, 20, 12)
  }
  
  /**
   * Retrieves the ID at the specified position.
   */
  override fun getId(x: Int, y: Int, z: Int) = data.get(toKey(x, y, z))

  /**
   * Sets the ID at the specified position.
   */
  override fun set(x: Int, y: Int, z: Int, id: Byte) {
    data.put(toKey(x, y, z), id)
  }
  
  /**
   * Checks if the positional storage contains the specified position.
   */
  override fun contains(x: Int, y: Int, z: Int) = data.containsKey(toKey(x, y, z))
  
  /**
   * Removes the value at the specified position.
   */
  override fun remove(x: Int, y: Int, z: Int) {
    data.remove(toKey(x, y, z))
  }
  
  /**
   * Writes the positional storage data to a [ByteBuf].
   *
   * @param buf The [ByteBuf] to write the data to.
   */
  override fun write(buf: ByteBuf) {
    buf.writeInt(data.size)
    data.int2ByteEntrySet().fastForEach {
      buf.writeInt(it.intKey)
      buf.writeByte(it.byteValue.toInt())
    }
  }
  
  /**
   * Reads the positional storage data from a [ByteBuf].
   *
   * @param buf The [ByteBuf] to read the data from.
   */
  override fun read(buf: ByteBuf, size: Int) {
    repeat(size) {
      data.put(buf.readInt(), buf.readByte())
    }
  }
}
