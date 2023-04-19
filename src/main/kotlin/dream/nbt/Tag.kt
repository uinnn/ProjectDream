package dream.nbt

import com.soywiz.korio.compression.*
import com.soywiz.korio.compression.deflate.*
import dream.misc.*
import dream.nbt.types.*
import dream.utils.*
import it.unimi.dsi.fastutil.io.*
import java.io.*

/**
 * Represents a abstract NBT tag.
 */
@Open
interface Tag : Serializable {
   
   /**
    * The generic value of this tag.
    */
   val genericValue: Any
   
   /**
    * The type of this NBT.
    */
   val type: TagType<out Tag>
   
   /**
    * Writes this NBT to the given [data] output.
    */
   fun write(data: ObjectOutput)
   
   /**
    * Makes a copy of this NBT.
    */
   fun copy(): Tag
   
   /**
    * Converts this NBT to a string.
    */
   override fun toString(): String
}

/**
 * Returns if this NBT is an end NBT: [EmptyTag].
 *
 * @see EmptyTag
 */
inline val Tag.isEnd: Boolean
   get() = type === EmptyType

/**
 * Returns the type id of this NBT.
 */
inline val Tag.id: Int
   get() = type.id.toInt()

/**
 * Converts this NBT compound to byte array data.
 */
fun Tag.toBytes(): ByteArray {
   val stream = FastByteArrayOutputStream()
   TagIO.write(DataOutputStream(stream), this)
   return stream.array
}

fun Tag.toCompressedBytes(
   method: CompressionMethod = ZLib,
   context: CompressionContext = CompressionContext(),
): ByteArray = toBytes().compress(method, context)

fun ByteArray.uncompressTag(method: CompressionMethod = ZLib) = uncompress(method).decodeTag()
fun ByteArray.uncompressCompound(method: CompressionMethod = ZLib) = uncompress(method).decodeCompound()

/**
 * Decodes this byte array to a tag.
 */
fun ByteArray.decodeTag() = TagIO.read(DataInputStream(fastInputStream()))
fun ByteArray.decodeCompound() = TagIO.read(DataInputStream(fastInputStream())) as CompoundTag

