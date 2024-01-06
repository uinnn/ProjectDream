package dream.packet.game

import dream.entity.base.*
import dream.network.*

class SPacketEntityVelocity(
  var entityId: Int,
  var motionX: Int,
  var motionY: Int,
  var motionZ: Int
) : ServerGamePacket {
  
  constructor(entity: Entity) : this(
    entity.serialId,
    checkSpeed(entity.motionX),
    checkSpeed(entity.motionY),
    checkSpeed(entity.motionZ)
  )
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readShort().toInt(),
    buf.readShort().toInt(),
    buf.readShort().toInt()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeShort(motionX)
    buf.writeShort(motionY)
    buf.writeShort(motionZ)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
