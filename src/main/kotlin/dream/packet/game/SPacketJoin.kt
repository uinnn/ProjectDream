package dream.packet.game

import com.soywiz.kmem.*
import dream.entity.player.*
import dream.level.*
import dream.network.*

/**
 * Serverbound player packet join.
 */
class SPacketJoin(
  var entityId: Int,
  var hardcore: Boolean,
  var gamemode: Gamemode,
  var dimension: Int,
  var difficulty: Difficulty,
  var maxPlayers: Int,
  var levelType: LevelType,
  var reducedDebugInfo: Boolean,
) : ServerGamePacket {

  constructor(buf: PacketBuffer, mode: Int) : this(
    entityId = buf.readInt(),
    hardcore = mode.hasFlags(8),
    gamemode = Gamemode.byId(mode and -9),
    dimension = buf.readByte().toInt(),
    difficulty = Difficulty.byId(buf.readUnsignedByte().toInt()),
    maxPlayers = buf.readUnsignedByte().toInt(),
    levelType = LevelType.DEFAULT,
    reducedDebugInfo = buf.readBoolean()
  )

  constructor(buf: PacketBuffer) : this(buf, buf.getUnsignedByte(1).toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeInt(entityId)

    var id = gamemode.id
    if (hardcore) id = id or 8
    buf.writeByte(id)

    buf.writeByte(dimension)
    buf.writeByte(difficulty.id)
    buf.writeByte(maxPlayers)
    buf.writeString(levelType.name)
    buf.writeBoolean(reducedDebugInfo)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }

}
