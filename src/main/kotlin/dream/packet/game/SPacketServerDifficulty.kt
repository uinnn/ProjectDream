package dream.packet.game

import dream.level.Difficulty
import dream.network.PacketBuffer

/**
 * Serverbound server difficulty packet.
 */
class SPacketServerDifficulty(var difficulty: Difficulty, var isLocked: Boolean = false) : ServerGamePacket {

  constructor(difficulty: Int, isLocked: Boolean = false) : this(Difficulty.byId(difficulty), isLocked)

  constructor(buf: PacketBuffer) : this(buf.readUnsignedByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeByte(difficulty.id)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
