package dream.packet.game

import dream.network.*

data class SPacketDisplayScoreboard(
  var position: Int,
  var name: String
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readByte().toInt(), buf.readString(16))
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(position)
    buf.writeString(name)
  }
}
