package dream.packet.status

import dream.network.*
import dream.utils.parseJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Serverbound Server info packet.
 *
 * Sended when player updates server list.
 */
class SPacketServerInfo(var response: ServerStatusResponse) : ServerStatusPacket {

  constructor(buf: PacketBuffer) : this(buf.readString().parseJson<ServerStatusResponse>())

  override fun write(buf: PacketBuffer) {
    buf.writeString(Json.encodeToString(response))
  }

  override fun process(handler: StatusPacketHandler) {
    handler.processServerInfo(this)
  }
}
