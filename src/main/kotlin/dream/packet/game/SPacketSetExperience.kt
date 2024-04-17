package dream.packet.game

import dream.network.*

data class SPacketSetExperience(
  var progress: Float,
  var experience: Int,
  var level: Int
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readFloat(), buf.readVarInt(), buf.readVarInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeFloat(progress)
    buf.writeVarInt(experience)
    buf.writeVarInt(level)
  }
}
