package dream.network

import dream.Sound
import dream.block.Block
import dream.block.Blocks
import dream.block.state.IState
import dream.chat.Component
import dream.collections.SizedList
import dream.entity.Watcher
import dream.entity.WatcherValue
import dream.item.EmptyItemStack
import dream.item.ItemStack
import dream.item.itemOf
import dream.misc.Open
import dream.nbt.TagIO
import dream.nbt.types.CompoundTag
import dream.pos.Pos
import dream.pos.Rot
import dream.utils.and
import dream.utils.or
import dream.utils.shl
import dream.village.MerchantTrade
import dream.village.MerchantTradeList
import io.netty.buffer.*
import io.netty.util.ByteProcessor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset
import java.util.*
import kotlin.reflect.KClass

/**
 * Represents a packet buffer for sending/receiving between connections
 */
@Open
class PacketBuffer(private val buf: ByteBuf) : ByteBuf() {

  constructor() : this(Unpooled.buffer())
  constructor(wrapped: ByteArray) : this(Unpooled.wrappedBuffer(wrapped))

  /**
   * Reads a compressed int from the buffer. To do so it maximally reads 5 byte-sized
   * chunks whose most significant bit dictates whether another byte should be read.
   */
  fun readVarInt(): Int {
    var i = 0
    var j = 0
    while (true) {
      val value = readByte()
      i = i or (value and 127 shl j++ * 7)
      if (value and 128 != 128.toByte()) break
    }
    return i
  }

  fun readVarLong(): Long {
    var i = 0L
    var j = 0
    while (true) {
      val value = readByte()
      i = i or ((value and 127) shl j++ * 7)
      if (value and 128 != 128.toByte()) break
    }
    return i
  }

  /**
   * Writes a compressed int to the buffer. The smallest number of bytes to fit the passed int will be written. Of
   * each such byte only 7 bits will be used to describe the actual value since its most significant bit dictates
   * whether the next byte is part of that same int. Micro-optimization for int values that are expected to have
   * values below 128.
   */
  fun writeVarInt(value: Int): PacketBuffer {
    var input = value
    while (input and -128 != 0) {
      writeByte(input and 127 or 128)
      input = input ushr 7
    }
    writeByte(input)
    return this
  }

  fun writeVarLong(value: Long): PacketBuffer {
    var input = value
    while (input and -128L != 0L) {
      writeByte((input and 127L).toInt() or 128)
      input = input ushr 7
    }
    writeByte(input.toInt())
    return this
  }

  fun writeByteArray(value: ByteArray): PacketBuffer {
    writeVarInt(value.size)
    writeBytes(value)
    return this
  }

  fun readByteArray(): ByteArray {
    val bytes = ByteArray(readVarInt())
    readBytes(bytes)
    return bytes
  }

  fun readBytes(): ByteBuf {
    return buf.readBytes(readableBytes())
  }

  fun <T> writeJson(serializer: KSerializer<T>, value: T, json: Json = Json): PacketBuffer {
    return writeString(json.encodeToString(serializer, value))
  }

  final inline fun <reified T : Any> writeJson(value: T, json: Json = Json): PacketBuffer {
    return writeString(json.encodeToString(value))
  }

  fun <T> readJson(serializer: KSerializer<T>, json: Json = Json): T {
    val string = readString()
    return json.decodeFromString(serializer, string)
  }

  final inline fun <reified T : Any> readJson(json: Json = Json): T {
    val string = readString()
    return json.decodeFromString(string)
  }

  fun writeString(value: String): PacketBuffer {
    val bytes = value.toByteArray()
    writeVarInt(bytes.size)
    writeBytes(bytes)
    return this
  }

  fun readString(): String {
    val size = readVarInt()
    return String(readBytes(size).array())
  }

  fun writeState(state: IState): PacketBuffer {
    writeVarInt(state.id)
    return this
  }

  fun readState(): IState {
    return Blocks.stateById(readVarInt())
  }

  fun writeBlock(block: Block): PacketBuffer {
    writeVarInt(block.id and 4095)
    return this
  }

  fun readBlock(): Block {
    return Blocks.byStateId(readVarInt())
  }

  fun readPos(): Pos {
    return Pos(readLong())
  }

  fun writePos(pos: Pos): PacketBuffer {
    writeLong(pos.asLong())
    return this
  }

  fun readVec(): Pos {
    return Pos(readFloat(), readFloat(), readFloat())
  }

  fun writeVec(pos: Pos): PacketBuffer {
    writeFloat(pos.x.toFloat())
    writeFloat(pos.y.toFloat())
    writeFloat(pos.z.toFloat())
    return this
  }

  fun readRotation(): Rot {
    return Rot(readFloat(), readFloat(), readFloat())
  }

  fun writeRotation(rot: Rot): PacketBuffer {
    writeFloat(rot.x)
    writeFloat(rot.y)
    writeFloat(rot.z)
    return this
  }

  fun writeUUID(uuid: UUID): PacketBuffer {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
    return this
  }

  fun readUUID(): UUID {
    return UUID(readLong(), readLong())
  }

  fun writeComponent(component: Component): PacketBuffer {
    writeString(component.toJson())
    return this
  }

  fun readComponent(): Component {
    return Component.fromString(readString())
  }

  fun writeCompound(tag: CompoundTag?): PacketBuffer {
    if (tag == null || tag.isEmpty()) writeByte(0)
    else TagIO.write(output(), tag)
    return this
  }

  fun readCompoundOrNull(): CompoundTag? {
    val index = readerIndex()

    return if (readByte().toInt() == 0) {
      null
    } else {
      readerIndex(index)
      TagIO.readCompound(input())
    }
  }

  fun readCompound(): CompoundTag {
    val index = readerIndex()

    return if (readByte().toInt() == 0) {
      CompoundTag()
    } else {
      readerIndex(index)
      TagIO.readCompound(input())
    }
  }

  fun writeItem(stack: ItemStack?): PacketBuffer {
    if (stack == null || stack.isAir) {
      writeShort(-1)
    } else {
      val item = stack.item
      val tag = if (item.isDamageable(stack) || item.shouldShareTag(stack)) stack.tag else null

      writeShort(item.id)
      writeByte(stack.amount)
      writeShort(stack.metadata)
      writeCompound(tag)
      item.writeToPacket(stack, this)
    }

    return this
  }

  fun readItem(): ItemStack {
    val id = readShort().toInt()
    if (id < 0) {
      return EmptyItemStack
    }

    val item = itemOf(id)
    val amount = readByte().toInt()
    val metadata = readShort().toInt()
    val tag = readCompound()
    val stack = ItemStack(item, amount, metadata, tag)

    item.readFromPacket(stack, this)

    return stack
  }

  fun <T : Enum<T>> writeEnum(enum: Enum<T>): PacketBuffer {
    writeVarInt(enum.ordinal)
    return this
  }

  fun <T : Enum<T>> readEnum(enum: Class<T>): T {
    return enum.enumConstants[readVarInt()]
  }

  fun <T : Enum<T>> readEnum(enum: KClass<T>): T {
    return enum.java.enumConstants[readVarInt()]
  }

  final inline fun <reified T : Enum<T>> readEnum(): T {
    return enumValues<T>()[readVarInt()]
  }

  fun <T> readList(limit: Int, reader: (PacketBuffer) -> T): List<T> {
    val list = SizedList<T>(limit)
    repeat(limit) {
      list.add(reader(this))
    }
    return list
  }

  fun <T, C : MutableList<T>> readList(destination: C, limit: Int, reader: (PacketBuffer) -> T): C {
    repeat(limit) {
      destination.add(reader(this))
    }
    return destination
  }

  fun <T> writeList(elements: Iterable<T>, writer: PacketBuffer.(T) -> Unit): PacketBuffer {
    for (element in elements) {
      writer(element)
    }
    return this
  }

  fun <T> writeList(elements: Iterable<T>, limit: Int, writer: PacketBuffer.(T) -> Unit): PacketBuffer {
    for ((index, element) in elements.withIndex()) {
      if (index == limit) {
        break
      }
      writer(element)
    }
    return this
  }

  fun writeIntArray(elements: IntArray, writer: PacketBuffer.(Int) -> Unit): PacketBuffer {
    for (element in elements) {
      writer(element)
    }
    return this
  }

  fun writeWatcher(watchers: List<WatcherValue>): PacketBuffer {
    Watcher.writeObjects(this, watchers)
    return this
  }

  fun readWatcher(): List<WatcherValue> {
    return Watcher.readObjects(this)
  }

  fun writeMerchantRecipeList(recipes: MerchantTradeList) {
    writeByte(recipes.size and 255)
    writeList(recipes) { recipe ->
      writeItem(recipe.buyItem)
      writeItem(recipe.sellItem)
      writeBoolean(recipe.hasSecondBuyItem)
      if (recipe.hasSecondBuyItem) {
        writeItem(recipe.secondBuyItem)
      }

      writeBoolean(recipe.isDisabled())
      writeInt(recipe.trades)
      writeInt(recipe.maxTrades)
    }
  }

  fun readMerchantRecipeList(): MerchantTradeList {
    val size = (readByte() and 255).toInt()
    return readList(MerchantTradeList(), size) {
      val buy = readItem()
      val sell = readItem()
      val secondBuy = if (readBoolean()) readItem() else EmptyItemStack
      val isDisabled = readBoolean()
      val trades = readInt()
      val maxTrades = readInt()
      val recipe = MerchantTrade(buy, sell, secondBuy, maxTrades, trades)
      if (isDisabled) {
        recipe.disable()
      }

      recipe
    }
  }

  fun writeSound(sound: Sound): PacketBuffer {
    writeString(sound.name)
    return this
  }

  fun readSoundOrNull(): Sound? {
    return Sound.byName(readString())
  }

  fun readSound(default: Sound = Sound.STEP_STONE): Sound {
    return readSoundOrNull() ?: default
  }

  fun output(): ByteBufOutputStream {
    return ByteBufOutputStream(this)
  }

  fun input(): ByteBufInputStream {
    return ByteBufInputStream(this)
  }

  fun readBuffer(bytes: Int = readableBytes()): PacketBuffer {
    return PacketBuffer(readBytes(bytes))
  }

  override fun capacity(): Int {
    return buf.capacity()
  }

  override fun capacity(capacity: Int): ByteBuf {
    return buf.capacity(capacity)
  }

  override fun maxCapacity(): Int {
    return buf.maxCapacity()
  }

  override fun alloc(): ByteBufAllocator {
    return buf.alloc()
  }

  @Deprecated("Deprecated in Java")
  override fun order(): ByteOrder? {
    return buf.order()
  }

  @Deprecated("Deprecated in Java")
  override fun order(endianness: ByteOrder?): ByteBuf {
    return buf.order(endianness)
  }

  override fun unwrap(): ByteBuf {
    return buf.unwrap()
  }

  override fun isDirect(): Boolean {
    return buf.isDirect
  }

  override fun isReadOnly(): Boolean {
    return buf.isReadOnly
  }

  override fun asReadOnly(): ByteBuf {
    return buf.asReadOnly()
  }

  override fun readerIndex(): Int {
    return buf.readerIndex()
  }

  override fun readerIndex(index: Int): ByteBuf {
    return buf.readerIndex(index)
  }

  override fun writerIndex(): Int {
    return buf.writerIndex()
  }

  override fun writerIndex(index: Int): ByteBuf {
    return buf.writerIndex(index)
  }

  override fun setIndex(readerIndex: Int, writerIndex: Int): ByteBuf {
    return buf.setIndex(readerIndex, writerIndex)
  }

  override fun readableBytes(): Int {
    return buf.readableBytes()
  }

  override fun writableBytes(): Int {
    return buf.writableBytes()
  }

  override fun maxWritableBytes(): Int {
    return buf.maxWritableBytes()
  }

  override fun isReadable(): Boolean {
    return buf.isReadable
  }

  override fun isReadable(size: Int): Boolean {
    return buf.isReadable(size)
  }

  override fun isWritable(): Boolean {
    return buf.isWritable
  }

  override fun isWritable(size: Int): Boolean {
    return buf.isWritable(size)
  }

  override fun clear(): ByteBuf {
    return buf.clear()
  }

  override fun markReaderIndex(): ByteBuf {
    return buf.markReaderIndex()
  }

  override fun resetReaderIndex(): ByteBuf {
    return buf.resetReaderIndex()
  }

  override fun markWriterIndex(): ByteBuf {
    return buf.markWriterIndex()
  }

  override fun resetWriterIndex(): ByteBuf {
    return buf.resetWriterIndex()
  }

  override fun discardReadBytes(): ByteBuf {
    return buf.discardReadBytes()
  }

  override fun discardSomeReadBytes(): ByteBuf {
    return buf.discardSomeReadBytes()
  }

  override fun ensureWritable(minWritableBytes: Int): ByteBuf {
    return buf.ensureWritable(minWritableBytes)
  }

  override fun ensureWritable(minWritableBytes: Int, force: Boolean): Int {
    return buf.ensureWritable(minWritableBytes, force)
  }

  override fun getBoolean(index: Int): Boolean {
    return buf.getBoolean(index)
  }

  override fun getByte(index: Int): Byte {
    return buf.getByte(index)
  }

  override fun getUnsignedByte(index: Int): Short {
    return buf.getUnsignedByte(index)
  }

  override fun getShort(index: Int): Short {
    return buf.getShort(index)
  }

  override fun getShortLE(index: Int): Short {
    return buf.getShortLE(index)
  }

  override fun getUnsignedShort(index: Int): Int {
    return buf.getUnsignedShort(index)
  }

  override fun getUnsignedShortLE(index: Int): Int {
    return buf.getUnsignedShortLE(index)
  }

  override fun getMedium(index: Int): Int {
    return buf.getMedium(index)
  }

  override fun getMediumLE(index: Int): Int {
    return buf.getMediumLE(index)
  }

  override fun getUnsignedMedium(index: Int): Int {
    return buf.getUnsignedMedium(index)
  }

  override fun getUnsignedMediumLE(index: Int): Int {
    return buf.getUnsignedMediumLE(index)
  }

  override fun getInt(index: Int): Int {
    return buf.getInt(index)
  }

  override fun getIntLE(index: Int): Int {
    return buf.getIntLE(index)
  }

  override fun getUnsignedInt(index: Int): Long {
    return buf.getUnsignedInt(index)
  }

  override fun getUnsignedIntLE(index: Int): Long {
    return buf.getUnsignedIntLE(index)
  }

  override fun getLong(index: Int): Long {
    return buf.getLong(index)
  }

  override fun getLongLE(index: Int): Long {
    return buf.getLongLE(index)
  }

  override fun getChar(index: Int): Char {
    return buf.getChar(index)
  }

  override fun getFloat(index: Int): Float {
    return buf.getFloat(index)
  }

  override fun getDouble(index: Int): Double {
    return buf.getDouble(index)
  }

  override fun getBytes(index: Int, dst: ByteBuf): ByteBuf {
    return buf.getBytes(index, dst)
  }

  override fun getBytes(index: Int, dst: ByteBuf, length: Int): ByteBuf {
    return buf.getBytes(index, dst, length)
  }

  override fun getBytes(index: Int, dst: ByteBuf?, dstIndex: Int, length: Int): ByteBuf {
    return buf.getBytes(index, dst, dstIndex, length)
  }

  override fun getBytes(index: Int, dst: ByteArray?): ByteBuf {
    return buf.getBytes(index, dst)
  }

  override fun getBytes(index: Int, dst: ByteArray?, dstIndex: Int, length: Int): ByteBuf {
    return buf.getBytes(index, dst, dstIndex, length)
  }

  override fun getBytes(index: Int, dst: ByteBuffer?): ByteBuf {
    return buf.getBytes(index, dst)
  }

  override fun getBytes(index: Int, out: OutputStream?, length: Int): ByteBuf {
    return buf.getBytes(index, out, length)
  }

  override fun getBytes(index: Int, out: GatheringByteChannel?, length: Int): Int {
    return buf.getBytes(index, out, length)
  }

  override fun getBytes(index: Int, out: FileChannel?, position: Long, length: Int): Int {
    return buf.getBytes(index, out, position, length)
  }

  override fun getCharSequence(index: Int, length: Int, charset: Charset?): CharSequence {
    return buf.getCharSequence(index, length, charset)
  }

  override fun setBoolean(index: Int, value: Boolean): PacketBuffer {
    buf.setBoolean(index, value)
    return this
  }

  override fun setByte(index: Int, value: Int): PacketBuffer {
    buf.setByte(index, value)
    return this
  }

  override fun setShort(index: Int, value: Int): PacketBuffer {
    buf.setShort(index, value)
    return this
  }

  override fun setShortLE(index: Int, value: Int): PacketBuffer {
    buf.setShortLE(index, value)
    return this
  }

  override fun setMedium(index: Int, value: Int): PacketBuffer {
    buf.setMedium(index, value)
    return this
  }

  override fun setMediumLE(index: Int, value: Int): PacketBuffer {
    buf.setMediumLE(index, value)
    return this
  }

  override fun setInt(index: Int, value: Int): PacketBuffer {
    buf.setInt(index, value)
    return this
  }

  override fun setIntLE(index: Int, value: Int): PacketBuffer {
    buf.setIntLE(index, value)
    return this
  }

  override fun setLong(index: Int, value: Long): PacketBuffer {
    buf.setLong(index, value)
    return this
  }

  override fun setLongLE(index: Int, value: Long): PacketBuffer {
    buf.setLongLE(index, value)
    return this
  }

  override fun setChar(index: Int, value: Int): PacketBuffer {
    buf.setChar(index, value)
    return this
  }

  override fun setFloat(index: Int, value: Float): PacketBuffer {
    buf.setFloat(index, value)
    return this
  }

  override fun setDouble(index: Int, value: Double): PacketBuffer {
    buf.setDouble(index, value)
    return this
  }

  override fun setBytes(index: Int, value: ByteBuf): PacketBuffer {
    buf.setBytes(index, value)
    return this
  }

  override fun setBytes(index: Int, src: ByteBuf?, length: Int): PacketBuffer {
    buf.setBytes(index, src, length)
    return this
  }

  override fun setBytes(index: Int, src: ByteBuf?, srcIndex: Int, length: Int): PacketBuffer {
    buf.setBytes(index, src, srcIndex, length)
    return this
  }

  override fun setBytes(index: Int, src: ByteArray?): PacketBuffer {
    buf.setBytes(index, src)
    return this
  }

  override fun setBytes(index: Int, src: ByteArray?, srcIndex: Int, length: Int): PacketBuffer {
    buf.setBytes(index, src, srcIndex, length)
    return this
  }

  override fun setBytes(index: Int, src: ByteBuffer?): PacketBuffer {
    buf.setBytes(index, src)
    return this
  }

  override fun setBytes(index: Int, `in`: InputStream?, length: Int): Int {
    return buf.setBytes(index, `in`, length)
  }

  override fun setBytes(index: Int, `in`: ScatteringByteChannel?, length: Int): Int {
    return buf.setBytes(index, `in`, length)
  }

  override fun setBytes(index: Int, `in`: FileChannel?, position: Long, length: Int): Int {
    return buf.setBytes(index, `in`, position, length)
  }

  override fun setZero(index: Int, length: Int): PacketBuffer {
    buf.setZero(index, length)
    return this
  }

  override fun setCharSequence(index: Int, sequence: CharSequence?, charset: Charset?): Int {
    return buf.setCharSequence(index, sequence, charset)
  }

  override fun readBoolean(): Boolean {
    return buf.readBoolean()
  }

  override fun readByte(): Byte {
    return buf.readByte()
  }

  override fun readUnsignedByte(): Short {
    return buf.readUnsignedByte()
  }

  override fun readShort(): Short {
    return buf.readShort()
  }

  override fun readShortLE(): Short {
    return buf.readShortLE()
  }

  override fun readUnsignedShort(): Int {
    return buf.readUnsignedShort()
  }

  override fun readUnsignedShortLE(): Int {
    return buf.readUnsignedShortLE()
  }

  override fun readMedium(): Int {
    return buf.readMedium()
  }

  override fun readMediumLE(): Int {
    return buf.readMediumLE()
  }

  override fun readUnsignedMedium(): Int {
    return buf.readUnsignedMedium()
  }

  override fun readUnsignedMediumLE(): Int {
    return buf.readUnsignedMediumLE()
  }

  override fun readInt(): Int {
    return buf.readInt()
  }

  override fun readIntLE(): Int {
    return buf.readIntLE()
  }

  override fun readUnsignedInt(): Long {
    return buf.readUnsignedInt()
  }

  override fun readUnsignedIntLE(): Long {
    return buf.readUnsignedIntLE()
  }

  override fun readLong(): Long {
    return buf.readLong()
  }

  override fun readLongLE(): Long {
    return buf.readLongLE()
  }

  override fun readChar(): Char {
    return buf.readChar()
  }

  override fun readFloat(): Float {
    return buf.readFloat()
  }

  override fun readDouble(): Double {
    return buf.readDouble()
  }

  override fun readBytes(length: Int): ByteBuf {
    return buf.readBytes(length)
  }

  override fun readSlice(length: Int): ByteBuf {
    return buf.readSlice(length)
  }

  override fun readRetainedSlice(length: Int): ByteBuf {
    return buf.readRetainedSlice(length)
  }

  override fun readCharSequence(length: Int, charset: Charset?): CharSequence {
    return buf.readCharSequence(length, charset)
  }

  override fun readBytes(dst: ByteBuf): PacketBuffer {
    buf.readBytes(dst)
    return this
  }

  override fun readBytes(dst: ByteBuf, length: Int): PacketBuffer {
    buf.readBytes(dst, length)
    return this
  }

  override fun readBytes(dst: ByteBuf, index: Int, length: Int): PacketBuffer {
    buf.readBytes(dst, index, length)
    return this
  }

  override fun readBytes(dst: ByteArray): PacketBuffer {
    buf.readBytes(dst)
    return this
  }

  override fun readBytes(dst: ByteArray, index: Int, length: Int): PacketBuffer {
    buf.readBytes(dst, index, length)
    return this
  }

  override fun readBytes(dst: ByteBuffer?): PacketBuffer {
    buf.readBytes(dst)
    return this
  }

  override fun readBytes(out: OutputStream?, length: Int): PacketBuffer {
    buf.readBytes(out, length)
    return this
  }

  override fun readBytes(out: GatheringByteChannel?, length: Int): Int {
    return buf.readBytes(out, length)
  }

  override fun readBytes(out: FileChannel?, position: Long, length: Int): Int {
    return buf.readBytes(out, position, length)
  }

  override fun skipBytes(length: Int): PacketBuffer {
    buf.skipBytes(length)
    return this
  }

  override fun writeBoolean(value: Boolean): PacketBuffer {
    buf.writeBoolean(value)
    return this
  }

  override fun writeByte(value: Int): PacketBuffer {
    buf.writeByte(value)
    return this
  }

  fun writeByte(value: Number): PacketBuffer {
    buf.writeByte(value.toInt())
    return this
  }

  override fun writeShort(value: Int): PacketBuffer {
    buf.writeShort(value)
    return this
  }

  fun writeShort(value: Number): PacketBuffer {
    buf.writeShort(value.toInt())
    return this
  }

  override fun writeShortLE(value: Int): PacketBuffer {
    buf.writeShortLE(value)
    return this
  }

  override fun writeMedium(value: Int): PacketBuffer {
    buf.writeMedium(value)
    return this
  }

  override fun writeMediumLE(value: Int): PacketBuffer {
    buf.writeMediumLE(value)
    return this
  }

  override fun writeInt(value: Int): PacketBuffer {
    buf.writeInt(value)
    return this
  }

  fun writeInt(value: Number): PacketBuffer {
    buf.writeInt(value.toInt())
    return this
  }

  override fun writeIntLE(value: Int): PacketBuffer {
    buf.writeIntLE(value)
    return this
  }

  override fun writeLong(value: Long): PacketBuffer {
    buf.writeLong(value)
    return this
  }

  fun writeLong(value: Number): PacketBuffer {
    buf.writeLong(value.toLong())
    return this
  }

  override fun writeLongLE(value: Long): PacketBuffer {
    buf.writeLongLE(value)
    return this
  }

  override fun writeChar(value: Int): PacketBuffer {
    buf.writeChar(value)
    return this
  }

  fun writeChar(value: Char): PacketBuffer {
    buf.writeChar(value.code)
    return this
  }

  override fun writeFloat(value: Float): PacketBuffer {
    buf.writeFloat(value)
    return this
  }

  fun writeFloat(value: Number): PacketBuffer {
    buf.writeFloat(value.toFloat())
    return this
  }

  override fun writeDouble(value: Double): PacketBuffer {
    buf.writeDouble(value)
    return this
  }

  fun writeDouble(value: Number): PacketBuffer {
    buf.writeDouble(value.toDouble())
    return this
  }

  override fun writeBytes(value: ByteBuf): PacketBuffer {
    buf.writeBytes(value)
    return this
  }

  override fun writeBytes(src: ByteBuf, length: Int): PacketBuffer {
    buf.writeBytes(src, length)
    return this
  }

  override fun writeBytes(src: ByteBuf, index: Int, length: Int): PacketBuffer {
    buf.writeBytes(src, index, length)
    return this
  }

  override fun writeBytes(src: ByteArray?): PacketBuffer {
    buf.writeBytes(src)
    return this
  }

  override fun writeBytes(src: ByteArray?, index: Int, length: Int): PacketBuffer {
    buf.writeBytes(src, index, length)
    return this
  }

  override fun writeBytes(src: ByteBuffer?): PacketBuffer {
    buf.writeBytes(src)
    return this
  }

  override fun writeBytes(`in`: InputStream?, length: Int): Int {
    return buf.writeBytes(`in`, length)
  }

  override fun writeBytes(`in`: ScatteringByteChannel?, length: Int): Int {
    return buf.writeBytes(`in`, length)
  }

  override fun writeBytes(`in`: FileChannel?, position: Long, length: Int): Int {
    return buf.writeBytes(`in`, position, length)
  }

  override fun writeZero(length: Int): PacketBuffer {
    buf.writeZero(length)
    return this
  }

  override fun writeCharSequence(sequence: CharSequence?, charset: Charset?): Int {
    return buf.writeCharSequence(sequence, charset)
  }

  override fun indexOf(fromIndex: Int, toIndex: Int, value: Byte): Int {
    return buf.indexOf(fromIndex, toIndex, value)
  }

  override fun bytesBefore(value: Byte): Int {
    return buf.bytesBefore(value)
  }

  override fun bytesBefore(length: Int, value: Byte): Int {
    return buf.bytesBefore(length, value)
  }

  override fun bytesBefore(index: Int, length: Int, value: Byte): Int {
    return buf.bytesBefore(index, length, value)
  }

  override fun forEachByte(processor: ByteProcessor?): Int {
    return buf.forEachByte(processor)
  }

  override fun forEachByte(index: Int, length: Int, processor: ByteProcessor?): Int {
    return buf.forEachByte(index, length, processor)
  }

  override fun forEachByteDesc(processor: ByteProcessor?): Int {
    return buf.forEachByteDesc(processor)
  }

  override fun forEachByteDesc(index: Int, length: Int, processor: ByteProcessor?): Int {
    return buf.forEachByteDesc(index, length, processor)
  }

  override fun copy(): ByteBuf {
    return buf.copy()
  }

  override fun copy(index: Int, length: Int): ByteBuf {
    return buf.copy(index, length)
  }

  override fun slice(): ByteBuf {
    return buf.slice()
  }

  override fun slice(index: Int, length: Int): ByteBuf {
    return buf.slice(index, length)
  }

  override fun retainedSlice(): ByteBuf {
    return buf.retainedSlice()
  }

  override fun retainedSlice(index: Int, length: Int): ByteBuf {
    return buf.retainedSlice(index, length)
  }

  override fun duplicate(): ByteBuf {
    return buf.duplicate()
  }

  override fun retainedDuplicate(): ByteBuf {
    return buf.retainedDuplicate()
  }

  override fun nioBufferCount(): Int {
    return buf.nioBufferCount()
  }

  override fun nioBuffer(): ByteBuffer {
    return buf.nioBuffer()
  }

  override fun nioBuffer(index: Int, length: Int): ByteBuffer {
    return buf.nioBuffer(index, length)
  }

  override fun internalNioBuffer(index: Int, length: Int): ByteBuffer {
    return buf.internalNioBuffer(index, length)
  }

  override fun nioBuffers(): Array<ByteBuffer> {
    return buf.nioBuffers()
  }

  override fun nioBuffers(index: Int, length: Int): Array<ByteBuffer> {
    return buf.nioBuffers(index, length)
  }

  override fun hasArray(): Boolean {
    return buf.hasArray()
  }

  override fun array(): ByteArray {
    return buf.array()
  }

  override fun arrayOffset(): Int {
    return buf.arrayOffset()
  }

  override fun hasMemoryAddress(): Boolean {
    return buf.hasMemoryAddress()
  }

  override fun memoryAddress(): Long {
    return buf.memoryAddress()
  }

  override fun toString(index: Int, length: Int, charset: Charset?): String {
    return buf.toString(index, length, charset)
  }

  override fun toString(charset: Charset?): String {
    return buf.toString(charset)
  }

  override fun hashCode(): Int {
    return buf.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    return buf == other
  }

  override fun compareTo(other: ByteBuf): Int {
    return buf.compareTo(other)
  }

  override fun toString(): String {
    return buf.toString()
  }

  override fun retain(increment: Int): ByteBuf {
    return buf.retain(increment)
  }

  override fun retain(): ByteBuf {
    return buf.retain()
  }

  override fun touch(): ByteBuf {
    return buf.touch()
  }

  override fun touch(hint: Any?): ByteBuf {
    return buf.touch(hint)
  }

  override fun refCnt(): Int {
    return buf.refCnt()
  }

  override fun release(): Boolean {
    return buf.release()
  }

  override fun release(decrement: Int): Boolean {
    return buf.release(decrement)
  }
}

/**
 * Creates a new packet buffer and applies [action].
 */
inline fun packetBuffer(action: PacketBuffer.() -> Unit) = PacketBuffer().apply(action)

/**
 * Creates a new packet buffer wrapped on this byte array.
 */
fun ByteBuf.toPacketBuffer() = PacketBuffer(this)

/**
 * Creates a new packet buffer wrapped on this byte array.
 */
inline fun ByteBuf.toPacketBuffer(action: PacketBuffer.() -> Unit): PacketBuffer {
  return PacketBuffer(this).apply(action)
}

/**
 * Creates a new packet buffer wrapped on this byte array.
 */
fun ByteArray.toPacketBuffer() = PacketBuffer(this)

/**
 * Creates a new packet buffer wrapped on this byte array.
 */
inline fun ByteArray.toPacketBuffer(action: PacketBuffer.() -> Unit): PacketBuffer {
  return PacketBuffer(this).apply(action)
}

/**
 * Returns a variable int size from the given [input].
 */
internal fun varInt(input: Int): Int {
  for (i in 1..4) {
    if (input and -1 shl i * 7 == 0) return i
  }

  return 5
}
