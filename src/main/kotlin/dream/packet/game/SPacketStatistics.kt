package dream.packet.game

import dream.network.*

data class SPacketStatistics(var stats: MutableMap<String, Int> = HashMap()) : ServerGamePacket {

  constructor(buf: PacketBuffer, size: Int) : this(HashMap(size)) {
    repeat(size) {
      stats[buf.readString()] = buf.readVarInt()
    }
  }

  constructor(buf: PacketBuffer) : this(buf, buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(stats.size)
    for (stat in stats) {
      buf.writeString(stat.key)
      buf.writeVarInt(stat.value)
    }
  }
}
