package dream.packet.game

import dream.network.*
import dream.pos.*

data class SPacketSignEditor(var pos: Pos) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readPos())

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
  }
}
