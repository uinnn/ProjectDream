@file:Suppress("NOTHING_TO_INLINE")

package dream.nbt

import dream.nbt.types.*
import it.unimi.dsi.fastutil.io.*
import java.io.*
import java.util.zip.*

/**
 * IO Utility for writing and reading NBT from file/streams.
 */
object TagIO {
   
   fun read(data: ObjectInputStream, close: Boolean = true): Tag {
      val type = TagRegistry[data.readByte()]
      val tag = type.load(data)
      if (close) data.close()
      return tag
   }
   
   /**
    * Reads a NBT in given [input].
    *
    * @param close optionally closes [input].
    */
   fun read(input: InputStream, close: Boolean = true): Tag {
      return read(input.toObjectNBTStream(), close)
   }
   
   /**
    * Reads a NBT in given [file].
    *
    * @param close optionally closes [file] stream.
    */
   fun read(file: File, close: Boolean = true): Tag = read(file.inputStream(), close)
   
   /**
    * Reads a [CompoundTag] in given [file].
    *
    * @param close optionally closes [file] stream.
    */
   fun readCompound(file: File, close: Boolean = true): CompoundTag = read(file, close) as CompoundTag
   
   /**
    * Reads a [CompoundTag] in given [input].
    *
    * @param close optionally closes [input].
    */
   fun readCompound(input: InputStream, close: Boolean = true): CompoundTag = read(input, close) as CompoundTag
   
   fun write(data: ObjectOutputStream, value: Tag, close: Boolean = true) {
      data.writeByte(value.id)
      value.write(data)
      if (close) data.close()
   }
   
   /**
    * Writes [value] in [output].
    *
    * @param close optionally closes [output].
    */
   fun write(output: OutputStream, value: Tag, close: Boolean = true) {
      write(output.toObjectNBTStream(), value, close)
   }
   
   /**
    * Writes [value] in [file].
    *
    * @param close optionally closes [file] stream.
    */
   fun write(file: File, value: Tag, close: Boolean = true) = write(file.outputStream(), value, close)
}

/**
 * Creates a NBT Stream using this output stream.
 */
fun OutputStream.toDataNBTStream() = DataOutputStream(FastBufferedOutputStream(GZIPOutputStream(this)))
fun OutputStream.toObjectNBTStream() = ObjectOutputStream(FastBufferedOutputStream(GZIPOutputStream(this)))

/**
 * Creates a NBT Stream using this input stream.
 */
fun InputStream.toDataNBTStream() = DataInputStream(FastBufferedInputStream(GZIPInputStream(this)))
fun InputStream.toObjectNBTStream() = ObjectInputStream(FastBufferedInputStream(GZIPInputStream(this)))

/**
 * Writes [tag] in this output stream.
 */
inline fun OutputStream.writeTag(tag: Tag, close: Boolean = true) = TagIO.write(this, tag, close)

/**
 * Reads a [CompoundTag] in this input stream.
 */
inline fun InputStream.readCompound(close: Boolean = true) = TagIO.readCompound(this, close)

/**
 * Writes [tag] in this file.
 */
inline fun File.writeTag(tag: Tag, close: Boolean = true) = TagIO.write(this, tag, close)

/**
 * Reads a [CompoundTag] in this file.
 */
inline fun File.readCompound(close: Boolean = true) = TagIO.readCompound(this, close)

/**
 * Reads a [Tag] in this input stream.
 */
inline fun InputStream.readTag(close: Boolean = true) = TagIO.read(this, close)

/**
 * Reads a [Tag] in this file.
 */
inline fun File.readTag(close: Boolean = true) = TagIO.read(this, close)
