package dream.packet.game

import dream.chat.*
import dream.network.*

data class SPacketPlayerListInfo(
  var header: ComponentText,
  var footer: ComponentText
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readComponent(), buf.readComponent())

  override fun write(buf: PacketBuffer) {
    buf.writeComponent(header)
    buf.writeComponent(footer)
  }
}
