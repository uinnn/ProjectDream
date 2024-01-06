package dream.packet.game

import dream.network.*

// TODO: SCOREBOARD
class SPacketUpdateScore : ServerGamePacket {
  
  constructor(buf: PacketBuffer)
  
  override fun write(buf: PacketBuffer) {
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
