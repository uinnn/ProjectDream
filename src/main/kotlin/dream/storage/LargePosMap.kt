package dream.storage

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap

typealias LongByteMap = Long2ByteOpenHashMap

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
 * key x-pos: *16.777.216* (24 bits)
 *
 * key y-pos: *256* (8 bits)
 *
 * key z-pos: *16.777.216* (24 bits)
 *
 * value: *256* (8 bits)
 */
abstract class LargePosMap<T : Any> : PosMap<T> {
  companion object {
    const val MAX_X = 16777216
    const val MAX_Y = 256
    const val MAX_Z = 16777216
    const val BYTES_PER_ENTRY = 8 // (56 bits long key) 7 + 1 (byte value)
  }

  /**
   * The data used to store.
   */
  lateinit var data: LongByteMap

  /**
   * Calculates the EXACT amount of bytes that's this map will occup.
   */
  override val bytes: Int get() = 4 + data.size * BYTES_PER_ENTRY

  constructor() : this(LongByteMap(8, 0.8F))

  constructor(data: LongByteMap) {
    this.data = data
  }

  constructor(buf: ByteBuf) {
    val size = buf.readInt()
    data = LongByteMap(size, 0.8F)
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
  fun toKey(x: Int, y: Int, z: Int): Long {
    return 0L.insert(x.toLong(), 0, 24).insert(y.toLong(), 24, 8).insert(z.toLong(), 32, 28)
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

  override fun clear() {
    data.clear()
  }

  /**
   * Writes the positional storage data to a [ByteBuf].
   *
   * @param buf The [ByteBuf] to write the data to.
   */
  override fun write(buf: ByteBuf) {
    buf.writeInt(data.size)
    data.long2ByteEntrySet().fastForEach {
      buf.write56Bits(it.longKey)
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
      data.put(buf.read56Bits(), buf.readByte())
    }
  }
}
