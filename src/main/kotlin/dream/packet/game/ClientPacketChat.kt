package dream.packet.game

import dream.network.*

/**
 * Clientbound packet chat.
 */
class ClientPacketChat(var message: String) : ClientGamePacket {
   
   // check for message char limit
   init {
      if (message.length > LIMIT) {
         message = message.substring(0, LIMIT)
      }
   }
   
   constructor(buf: PacketBuffer) : this(buf.readString())
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(message)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
   
   companion object {
      
      /**
       * The limit of chars on a single message.
       */
      const val LIMIT = 100
   }
}
