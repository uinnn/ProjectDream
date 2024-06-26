package dream.packet.status

import dream.network.*

/**
 * Serverbound Pong packet.
 */
data class SPacketPong(var clientTime: Long) : ServerStatusPacket {

  constructor(buf: PacketBuffer) : this(buf.readLong())

  override fun write(buf: PacketBuffer) {
    buf.writeLong(clientTime)
  }

  override fun process(handler: StatusPacketHandler) {
  }
}
