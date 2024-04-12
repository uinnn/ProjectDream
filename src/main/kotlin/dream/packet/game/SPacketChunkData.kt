package dream.packet.game

import dream.level.chunk.Chunk
import dream.level.chunk.storage.Pallete
import dream.network.PacketBuffer

/**
 * Serverbound packet to send chunk data to client.
 */
class SPacketChunkData(
  var chunkX: Int,
  var chunkZ: Int,
  var continuous: Boolean,
  var extracted: ExtractedChunk
) : ServerGamePacket {

  constructor(chunk: Chunk, continuous: Boolean, sections: Int) : this(
    chunk.x, chunk.z, continuous, ExtractedChunk.extract(chunk, continuous, false, sections)
  )

  constructor(buf: PacketBuffer) : this(buf.readInt(), buf.readInt(), buf.readBoolean(), ExtractedChunk(buf))

  override fun write(buf: PacketBuffer) {
    buf.writeInt(chunkX)
    buf.writeInt(chunkZ)
    buf.writeBoolean(continuous)
    buf.writeShort(extracted.size and 65535)
    buf.writeByteArray(extracted.data)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

/**
 * Extracted chunk to ByteArray used in [SPacketChunkData].
 */
data class ExtractedChunk(var size: Int, var data: ByteArray) {
  constructor() : this(0, ByteArray(0))
  constructor(buf: PacketBuffer) : this(buf.readShort().toInt(), buf.readByteArray())

  companion object {

    /**
     * Extracts the chunk data used to serialize them to packets sended to client.
     */
    fun extract(chunk: Chunk, continuous: Boolean, skylight: Boolean, sections: Int): ExtractedChunk {
      val extracted = ExtractedChunk()
      val palletes = chunk.palletes
      val allowedPalletes = ArrayList<Pallete>()
      palletes.forEachIndexed { index, pallete ->
        if ((!continuous || !pallete.isEmpty) && (sections and 1 shl index) != 0) {
          extracted.size = extracted.size or 1 shl index
          allowedPalletes.add(pallete)
        }
      }

      extracted.data = ByteArray(extractedChunkSize(extracted.size.countOneBits(), skylight, continuous))
      var size = 0

      for (pallete in allowedPalletes) {
        for (data in pallete.data) {
          val code = data.code
          extracted.data[size++] = (code and 255).toByte()
          extracted.data[size++] = (code shr 8 and 255).toByte()
        }
      }

      for (pallete in allowedPalletes) {
        size = copy(pallete.blocklight.data, extracted.data, size)
      }

      if (skylight) {
        for (pallete in allowedPalletes) {
          size = copy(pallete.skylight!!.data, extracted.data, size)
        }
      }

      if (continuous) {
        copy(chunk.biomes, extracted.data, size)
      }

      return extracted
    }
  }
}

/**
 * Makes a copy of [src] to [dest] and returns the length of the copy.
 */
internal fun copy(src: ByteArray, dest: ByteArray, length: Int): Int {
  System.arraycopy(src, 0, dest, length, src.size)
  return length + src.size
}

/**
 * Extracts chunk size from [sections], [skylight] and [biomes].
 */
internal fun extractedChunkSize(sections: Int, skylight: Boolean, biomes: Boolean): Int {
  val i = sections * 2 * 16 * 16 * 16
  val j = sections * 16 * 16 * 16 / 2
  val k = if (skylight) sections * 16 * 16 * 16 / 2 else 0
  val l = if (biomes) 256 else 0
  return i + j + k + l
}
