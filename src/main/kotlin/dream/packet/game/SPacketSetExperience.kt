package dream.packet.game

import dream.network.*

class SPacketSetExperience(
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
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
