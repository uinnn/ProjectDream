package dream.packet.game

import dream.network.PacketBuffer

/**
 * Clientbound packet animation.
 *
 * Empty packet, only used for swing player arms.
 */
object CPacketAnimation : ClientGamePacket {

  //constructor(buf: PacketBuffer) : this()

  override fun write(buf: PacketBuffer) {
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleAnimation(this)
  }
}
