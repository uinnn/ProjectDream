package dream.packet.game

import dream.network.*

/**
 * Clientbound packet payload.
 */
class ClientPacketPayload(
   var channel: String,
   var data: PacketBuffer,
) : ClientGamePacket {
   
   constructor(buf: PacketBuffer) : this(
      buf.readString(),
      PacketBuffer(buf.readBytes())
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(channel)
      buf.writeBytes(data)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}
