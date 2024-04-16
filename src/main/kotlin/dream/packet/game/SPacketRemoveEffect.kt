package dream.packet.game

import dream.network.*

data class SPacketRemoveEffect(var entityId: Int, var effectId: Int) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readUnsignedByte().toInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(effectId)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
