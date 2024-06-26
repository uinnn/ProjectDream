package dream.packet.status

import dream.network.*

/**
 * Clientbound Ping packet.
 */
data class CPacketPing(var clientTime: Long) : ClientStatusPacket {

  constructor(buf: PacketBuffer) : this(buf.readLong())

  override fun write(buf: PacketBuffer) {
    buf.writeLong(clientTime)
  }

  override fun process(handler: StatusPacketHandler) {
    handler.handlePing(this)
  }
}
