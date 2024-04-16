package dream.packet.game

import dream.network.PacketBuffer

/**
 * Clientbound packet chat.
 */
data class CPacketChat(var message: String) : ClientGamePacket {

  // check for message char limit
  init {
    if (message.length > LIMIT) {
      message = message.substring(0, LIMIT)
    }
  }

  constructor(buf: PacketBuffer) : this(buf.readString())

  override fun write(buf: PacketBuffer) {
    buf.writeString(message)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleChat(this)
  }

  companion object {

    /**
     * The limit of chars on a single message.
     */
    const val LIMIT = 100
  }
}
