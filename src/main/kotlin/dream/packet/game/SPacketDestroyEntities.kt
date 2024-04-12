package dream.packet.game

import dream.entity.base.Entity
import dream.network.PacketBuffer
import korlibs.datastructure.mapInt

class SPacketDestroyEntities(var entities: IntArray) : ServerGamePacket {
  
  //constructor(vararg entities: Int) : this(entities)
  
  constructor(vararg entities: Entity) : this(entities.mapInt { it.serialId })
  
  constructor(buf: PacketBuffer) : this(IntArray(buf.readVarInt()) { buf.readVarInt() })
  
  override fun write(buf: PacketBuffer) {
    buf.writeIntArray(entities) { writeVarInt(it) }
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
