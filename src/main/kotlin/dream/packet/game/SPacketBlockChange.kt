package dream.packet.game

import dream.block.state.*
import dream.level.*
import dream.network.*
import dream.pos.*

/**
 * Serverbound packet for block change
 */
data class SPacketBlockChange(var pos: Pos, var state: IState) : ServerGamePacket {
  
  constructor(level: Level, pos: Pos) : this(pos, level.getState(pos))
  constructor(buf: PacketBuffer) : this(buf.readPos(), buf.readState())
  
  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeState(state)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
