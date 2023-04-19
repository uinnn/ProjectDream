package dream.packet.status

import dream.network.*
import dream.utils.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Serverbound Server info packet.
 *
 * Sended when player updates server list.
 */
class ServerPacketServerInfo(var response: ServerStatusResponse) : ServerStatusPacket {
   
   constructor(buf: PacketBuffer) : this(buf.readString().parseJson<ServerStatusResponse>())
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(Json.encodeToString(response))
   }
   
   override fun process(handler: StatusPacketHandler) {
      handler.processServerInfo(this)
   }
}
