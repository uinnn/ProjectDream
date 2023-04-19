package dream.packet.status

import dream.network.*

/**
 * Clientbound Ping packet.
 */
class ClientPacketPing(var clientTime: Long) : ClientStatusPacket {
   
   constructor(buf: PacketBuffer) : this(buf.readLong())
   
   override fun write(buf: PacketBuffer) {
      buf.writeLong(clientTime)
   }
   
   override fun process(handler: StatusPacketHandler) {
      handler.handlePing(this)
   }
}
