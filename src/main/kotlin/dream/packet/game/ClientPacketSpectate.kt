package dream.packet.game

import dream.entity.*
import dream.level.*
import dream.network.*
import java.util.*

/**
 * Clientbound packet spectate.
 */
class ClientPacketSpectate(var id: UUID) : ClientGamePacket {
   
   constructor(entity: Entity) : this(entity.id)
   
   constructor(buf: PacketBuffer) : this(buf.readUUID())
   
   override fun write(buf: PacketBuffer) {
      buf.writeUUID(id)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
   
   /**
    * Gets the entity associated to this packet.
    */
   fun entity(level: Level): Entity? {
      return level.getEntity(id)
   }
}
