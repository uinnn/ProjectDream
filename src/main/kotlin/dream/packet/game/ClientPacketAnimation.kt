package dream.packet.game

import dream.network.*

/**
 * Clientbound packet animation.
 *
 * Empty packet.
 */
class ClientPacketAnimation() : ClientGamePacket {
   
   constructor(buf: PacketBuffer) : this()
   
   override fun write(buf: PacketBuffer) {
   
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}
