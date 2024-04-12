package dream.packet.status

import dream.network.PacketBuffer

/**
 * Clientbound Server query packet.
 *
 * Empty packet.
 */
object CPacketServerQuery : ClientStatusPacket {
  override fun write(buf: PacketBuffer) {
  }

  override fun process(handler: StatusPacketHandler) {
    handler.handleServerQuery(this)
  }
}
