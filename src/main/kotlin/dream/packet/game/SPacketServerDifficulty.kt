package dream.packet.game

import dream.level.*
import dream.network.*

/**
 * Serverbound server difficulty packet.
 */
data class SPacketServerDifficulty(var difficulty: Difficulty, var isLocked: Boolean = false) : ServerGamePacket {

  constructor(difficulty: Int, isLocked: Boolean = false) : this(Difficulty.byId(difficulty), isLocked)

  constructor(buf: PacketBuffer) : this(buf.readUnsignedByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeByte(difficulty.id)
  }
}
