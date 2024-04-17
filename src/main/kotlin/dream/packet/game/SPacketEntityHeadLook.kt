package dream.packet.game

import dream.network.*

data class SPacketEntityHeadLook(var entityId: Int, var yaw: Byte) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readByte())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(yaw)
  }
}
