package dream.packet.status

import dream.network.*

/**
 * Serverbound Pong packet.
 */
class ServerPacketPong(var clientTime: Long) : ServerStatusPacket {
   
   constructor(buf: PacketBuffer) : this(buf.readLong())
   
   override fun write(buf: PacketBuffer) {
      buf.writeLong(clientTime)
   }
   
   override fun process(handler: StatusPacketHandler) {
      handler.processPong(this)
   }
}
