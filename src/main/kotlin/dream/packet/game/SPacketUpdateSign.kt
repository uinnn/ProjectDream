package dream.packet.game

import dream.chat.*
import dream.network.*
import dream.pos.*

data class SPacketUpdateSign(
  var pos: Pos,
  var lines: MutableList<ComponentText>
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readPos(), buf.readList(ArrayList(), 4) { it.readComponent() })

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    repeat(4) {
      buf.writeComponent(lines[it])
    }
  }
}
