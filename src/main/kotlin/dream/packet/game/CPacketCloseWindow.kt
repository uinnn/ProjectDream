package dream.packet.game

import dream.inventory.container.Container
import dream.network.PacketBuffer

/**
 * CLientbound close window.
 */
data class CPacketCloseWindow(var id: Int) : ClientGamePacket {

  constructor(container: Container) : this(container.id)

  constructor(buf: PacketBuffer) : this(buf.readByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeByte(id)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleCloseWindow(this)
  }
}
