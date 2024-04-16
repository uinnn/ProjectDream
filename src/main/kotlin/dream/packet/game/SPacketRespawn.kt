package dream.packet.game

import dream.entity.player.*
import dream.level.*
import dream.network.*

data class SPacketRespawn(
  var dimension: Dimension,
  var difficulty: Difficulty,
  var gamemode: Gamemode,
  var levelType: LevelType
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(
    dimension = Dimension.byId(buf.readInt()),
    difficulty = Difficulty.byId(buf.readUnsignedByte().toInt()),
    gamemode = Gamemode.byId(buf.readUnsignedByte().toInt()),
    levelType = LevelType.DEFAULT // TODO: setup level type
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeInt(dimension.id)
    buf.writeByte(difficulty.id)
    buf.writeByte(gamemode.id)
    buf.writeString(levelType.name)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
  
}
