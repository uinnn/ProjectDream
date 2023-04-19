package dream.packet.login

import dream.network.*
import dream.utils.*

/**
 * Clientbound Login start packet.
 */
class ClientPacketLoginStart(var profile: Profile) : ClientLoginPacket {
   
   constructor(buf: PacketBuffer) : this(Profile(null, buf.readString()))
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(profile.name)
   }
   
   override fun process(handler: LoginPacketHandler) {
      handler.handleLoginStart(this)
   }
}
