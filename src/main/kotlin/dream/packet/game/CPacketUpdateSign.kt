package dream.packet.game

import dream.chat.*
import dream.network.*
import dream.pos.*

/**
 * Clientbound packet update sign.
 *
 * Used to update sign lines.
 */
data class CPacketUpdateSign(var pos: Pos, var lines: List<ComponentText>) : ClientGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readPos(),
    buf.readList(4, PacketBuffer::readComponent)
  )

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeList(lines, 4, PacketBuffer::writeComponent)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleUpdateSign(this)
  }
}
