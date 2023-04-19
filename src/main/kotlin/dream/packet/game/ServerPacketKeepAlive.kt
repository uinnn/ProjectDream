package dream.packet.game

import dream.entity.*
import dream.network.*

/**
 * Serverbound Keep alive connection packet.
 */
class ServerPacketKeepAlive(var entityId: Int) : ServerGamePacket {
   
   constructor(entity: Entity) : this(entity.serialId)
   
   constructor(buf: PacketBuffer) : this(buf.readVarInt())
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(entityId)
   }
   
   override fun process(handler: GamePacketHandler) {
      TODO("Not yet implemented")
   }
}
