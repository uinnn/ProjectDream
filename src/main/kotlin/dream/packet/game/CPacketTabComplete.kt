package dream.packet.game

import dream.network.*
import dream.pos.*

/**
 * Clientbound packet tab complete.
 */
class CPacketTabComplete(var message: String, var targetPos: Pos? = null) : ClientGamePacket {

  /**
   * Gets if this packet has a target pos.
   */
  val hasTargetPos: Boolean get() = targetPos != null

  constructor(buf: PacketBuffer) : this(buf.readString()) {
    if (buf.readBoolean()) {
      targetPos = buf.readPos()
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeString(message)
    buf.writeBoolean(hasTargetPos)
    if (hasTargetPos) {
      buf.writePos(targetPos!!)
    }
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleTabComplete(this)
  }
}
