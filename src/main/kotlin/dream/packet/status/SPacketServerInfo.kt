package dream.packet.status

import dream.network.PacketBuffer
import dream.network.ServerStatusResponse

/**
 * Serverbound Server info packet.
 *
 * Sended when player updates server list.
 */
data class SPacketServerInfo(var response: ServerStatusResponse) : ServerStatusPacket {

  constructor(buf: PacketBuffer) : this(buf.readJson<ServerStatusResponse>())

  override fun write(buf: PacketBuffer) {
    buf.writeJson(response)
  }

  override fun process(handler: StatusPacketHandler) {
  }
}
