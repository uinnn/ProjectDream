package dream.packet.status

import dream.network.*

/**
 * Clientbound Server query packet.
 *
 * Empty packet.
 */
class ClientPacketServerQuery() : ClientStatusPacket {
   
   constructor(buf: PacketBuffer) : this()
   
   override fun write(buf: PacketBuffer) {
   }
   
   override fun process(handler: StatusPacketHandler) {
      handler.handleServerQuery(this)
   }
}
