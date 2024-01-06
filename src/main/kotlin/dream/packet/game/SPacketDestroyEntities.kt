package dream.packet.game

import com.soywiz.kds.*
import dream.entity.base.*
import dream.network.*

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
