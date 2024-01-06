package dream.packet.game

import dream.network.*

/**
 * Clientbound packet animation.
 *
 * Empty packet, only used for swing player arms.
 */
class CPacketAnimation() : ClientGamePacket {

  constructor(buf: PacketBuffer) : this()

  override fun write(buf: PacketBuffer) {
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleAnimation(this)
  }
}
