package dream.packet.game

import dream.entity.*
import dream.network.*

/**
 * Serverbound Use bed packet.
 *
 * Called when player uses a bed.
 */
class ServerPacketUseBed(var entityId: Int) : ServerGamePacket {
   
   constructor(entity: Entity) : this(entity.serialId)
   
   constructor(buf: PacketBuffer) : this(buf.readVarInt())
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(entityId)
   }
   
   override fun process(handler: GamePacketHandler) {
      TODO("Not yet implemented")
   }
}
