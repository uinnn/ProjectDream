package dream.packet.game

import dream.network.*

class SPacketTimeUpdate(var totalTime: Long, var time: Long, doDaylightCycle: Boolean = true) : ServerGamePacket {
  
  init {
    if (!doDaylightCycle) {
      time = -time
      if (time == 0L)
        time = -1
    }
  }
  
  constructor(buf: PacketBuffer) : this(buf.readLong(), buf.readLong())
  
  override fun write(buf: PacketBuffer) {
    buf.writeLong(totalTime)
    buf.writeLong(time)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
