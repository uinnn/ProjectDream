package dream.packet.game

import dream.entity.*
import dream.network.*

/**
 * Clientbound Keep alive connection packet.
 */
class ClientPacketKeepAlive(var id: Int) : ClientGamePacket {
   
   constructor(entity: Entity) : this(entity.serialId)
   
   constructor(buf: PacketBuffer) : this(buf.readVarInt())
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(id)
   }
   
   override fun process(handler: GamePacketHandler) {
      TODO("Not yet implemented")
   }
}

