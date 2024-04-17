package dream.packet.game

import dream.network.*

data class SPacketCompressionLevel(var threshold: Int) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(threshold)
  }
}
