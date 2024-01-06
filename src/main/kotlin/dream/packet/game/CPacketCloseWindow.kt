package dream.packet.game

import dream.inventory.container.*
import dream.network.*

/**
 * CLientbound close window.
 */
class CPacketCloseWindow(var id: Int) : ClientGamePacket {

  constructor(container: Container) : this(container.id)

  constructor(buf: PacketBuffer) : this(buf.readByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeByte(id)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleCloseWindow(this)
  }
}
