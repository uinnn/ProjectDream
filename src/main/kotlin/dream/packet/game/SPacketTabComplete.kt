package dream.packet.game

import dream.network.*

data class SPacketTabComplete(var matches: List<String>) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readList(buf.readVarInt()) { it.readString() })
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(matches.size)
    buf.writeList(matches) { writeString(it) }
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
