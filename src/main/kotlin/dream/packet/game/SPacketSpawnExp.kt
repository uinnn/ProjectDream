package dream.packet.game

import dream.entity.item.*
import dream.network.*

class SPacketSpawnExp(
  var entityId: Int,
  var x: Int,
  var y: Int,
  var z: Int,
  var experience: Int,
) : ServerGamePacket {
  
  constructor(orb: ExperienceOrb) : this(
    orb.serialId,
    posToPacket(orb.x),
    posToPacket(orb.y),
    posToPacket(orb.z),
    orb.experience
  )
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readShort().toInt()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(y)
    buf.writeShort(experience)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
