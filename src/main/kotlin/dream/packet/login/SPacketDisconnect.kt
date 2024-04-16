package dream.packet.login

import dream.chat.*
import dream.network.*

/**
 * Serverbound Disconnect packet.
 */
data class SPacketDisconnect(var reason: ComponentText) : ServerLoginPacket {

  constructor(buf: PacketBuffer) : this(buf.readComponent())

  constructor(reason: String) : this(text(reason))

  override fun write(buf: PacketBuffer) {
    buf.writeComponent(reason)
  }

  override fun process(handler: LoginPacketHandler) {
  }
}
