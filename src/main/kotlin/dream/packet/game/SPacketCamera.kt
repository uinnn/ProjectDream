package dream.packet.game

import dream.entity.base.*
import dream.network.*

data class SPacketCamera(var entityId: Int) : ServerGamePacket {
  
  constructor(entity: Entity) : this(entity.serialId)
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
  }
}
