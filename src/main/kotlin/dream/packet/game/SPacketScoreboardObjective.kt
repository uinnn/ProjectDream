package dream.packet.game

import dream.network.*

// TODO: SCOREBOARD
class SPacketScoreboardObjective : ServerGamePacket {
  
  constructor(buf: PacketBuffer)
  
  override fun write(buf: PacketBuffer) {
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

