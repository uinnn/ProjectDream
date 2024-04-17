package dream.packet.game

import dream.entity.base.*
import dream.network.*
import korlibs.datastructure.*

data class SPacketDestroyEntities(var entities: IntArray) : ServerGamePacket {
  
  //constructor(vararg entities: Int) : this(entities)
  
  constructor(vararg entities: Entity) : this(entities.mapInt { it.serialId })
  
  constructor(buf: PacketBuffer) : this(IntArray(buf.readVarInt()) { buf.readVarInt() })
  
  override fun write(buf: PacketBuffer) {
    buf.writeIntArray(entities) { writeVarInt(it) }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SPacketDestroyEntities) return false
    return entities.contentEquals(other.entities)
  }

  override fun hashCode(): Int {
    return entities.contentHashCode()
  }
}
