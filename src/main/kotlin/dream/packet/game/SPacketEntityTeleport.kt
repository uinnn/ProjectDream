package dream.packet.game

import dream.entity.base.*
import dream.network.*

data class SPacketEntityTeleport(
  var entityId: Int,
  val x: Int,
  val y: Int,
  val z: Int,
  val yaw: Byte,
  val pitch: Byte,
  val onGround: Boolean,
) : ServerGamePacket {
  
  constructor(entity: Entity) : this(
    entity.serialId,
    posToPacket(entity.x),
    posToPacket(entity.y),
    posToPacket(entity.z),
    rotationToPacketByte(entity.yaw),
    rotationToPacketByte(entity.pitch),
    entity.onGround
  )
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readByte(),
    buf.readByte(),
    buf.readBoolean()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeByte(yaw)
    buf.writeByte(pitch)
    buf.writeBoolean(onGround)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
