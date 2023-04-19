package dream.packet.login

import com.mojang.authlib.*
import dream.network.*
import dream.utils.*

/**
 * Serverbound Login success packet.
 */
class ServerPacketLoginSuccess(var profile: GameProfile) : ServerLoginPacket {
   
   constructor(buf: PacketBuffer) : this(Profile(uuid(buf.readString()), buf.readString()))
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(profile.id?.toString() ?: "")
      buf.writeString(profile.name)
   }
   
   override fun process(handler: LoginPacketHandler) {
      handler.processLoginSuccess(this)
   }
}
