package dream.packet.game

import dream.network.PacketBuffer
import dream.utils.and

/**
 * Serverbound packet for chunk bulk.
 */
data class SPacketChunkBulk(
  var posX: IntArray,
  var posZ: IntArray,
  var chunks: Array<ExtractedChunk>,
  var hasSky: Boolean
) : ServerGamePacket {

  constructor(buf: PacketBuffer, hasSky: Boolean, size: Int) : this(
    IntArray(size),
    IntArray(size),
    Array(size) { ExtractedChunk() },
    hasSky
  ) {
    readBulkChunks(buf, size, posX, posZ, chunks, hasSky)
  }

  constructor(buf: PacketBuffer) : this(buf, buf.readBoolean(), buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeBoolean(hasSky)
    buf.writeVarInt(chunks.size)

    repeat(posX.size) {
      buf.writeInt(posX[it])
      buf.writeInt(posZ[it])
      buf.writeShort(chunks[it].size and 65535)
    }

    repeat(posX.size) {
      buf.writeByteArray(chunks[it].data)
    }
  }
}

internal fun readBulkChunks(
  buf: PacketBuffer,
  size: Int,
  x: IntArray,
  z: IntArray,
  chunks: Array<ExtractedChunk>,
  hasSky: Boolean
) {
  repeat(size) {
    x[it] = buf.readInt()
    z[it] = buf.readInt()
    val chunk = chunks[it]
    chunk.size = (buf.readShort() and 65535).toInt()
    chunk.data = ByteArray(extractedChunkSize(chunk.size, hasSky, true))
  }

  repeat(size) {
    buf.readBytes(chunks[it].data)
  }
}
