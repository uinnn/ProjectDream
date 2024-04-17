package dream.packet.game

import dream.network.*

data class SPacketEntityStatus(var entityId: Int, var code: Byte) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readByte())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(code)
  }
}
