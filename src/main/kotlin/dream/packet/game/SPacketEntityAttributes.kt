package dream.packet.game

import dream.network.*

class SPacketEntityAttributes(
  var entityId: Int,
) : ServerGamePacket {
  
  
  
  override fun write(buf: PacketBuffer) {
    TODO("Not yet implemented")
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
