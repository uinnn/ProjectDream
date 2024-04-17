package dream.packet.game

import dream.network.*

data class SPacketResourcePack(
  var url: String,
  var hash: String
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readString(), buf.readString(40))

  override fun write(buf: PacketBuffer) {
    buf.writeString(url)
    buf.writeString(hash)
  }
}

