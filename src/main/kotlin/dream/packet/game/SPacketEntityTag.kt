package dream.packet.game

import dream.entity.base.*
import dream.level.*
import dream.nbt.types.*
import dream.network.*

data class SPacketEntityTag(var entityId: Int, var tag: CompoundTag) : ServerGamePacket {
  
  constructor(entity: Entity, tag: CompoundTag) : this(entity.serialId, tag)
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readCompound())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeCompound(tag)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
  fun entity(level: Level): Entity? {
    return level.getEntity(entityId)
  }
  
}
