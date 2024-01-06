package dream.packet.login

import dream.chat.*
import dream.network.*

/**
 * Serverbound Disconnect packet.
 */
class SPacketDisconnect(var reason: Component) : ServerLoginPacket {

  constructor(buf: PacketBuffer) : this(buf.readComponent())

  constructor(reason: String) : this(text(reason))

  override fun write(buf: PacketBuffer) {
    buf.writeComponent(reason)
  }

  override fun process(handler: LoginPacketHandler) {
    handler.processDisconnect(this)
  }
}
