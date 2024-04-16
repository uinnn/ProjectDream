package dream.packet.game

import dream.network.*
import dream.pos.*

data class SPacketSpawnPos(var pos: Pos) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readPos())
  
  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
