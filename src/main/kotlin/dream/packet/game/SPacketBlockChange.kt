package dream.packet.game

import dream.block.*
import dream.block.state.*
import dream.level.*
import dream.network.*
import dream.pos.*

class SPacketBlockChange(var pos: Pos, var state: IState) : ServerGamePacket {
  
  constructor(level: Level, pos: Pos) : this(pos, level.getState(pos))
  constructor(buf: PacketBuffer) : this(buf.readPos(), Blocks.stateById(buf.readVarInt()))
  
  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeVarInt(state.id)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
