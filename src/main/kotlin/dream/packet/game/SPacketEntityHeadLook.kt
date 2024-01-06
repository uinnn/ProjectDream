package dream.packet.game

import dream.network.*

class SPacketEntityHeadLook(var entityId: Int, var yaw: Byte) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readByte())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(yaw)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
