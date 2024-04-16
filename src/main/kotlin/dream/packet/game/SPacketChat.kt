package dream.packet.game

import dream.chat.*
import dream.entity.player.*
import dream.network.*

/**
 * Serverbound Chat packet.
 */
data class SPacketChat(var message: ComponentText, var type: ChatType = ChatType.CHAT) : ServerGamePacket {

  /**
   * Gets if this packet chat type is considered a chat in vanilla.
   */
  val isChat: Boolean get() = type.isChat

  constructor(message: ComponentText, type: Byte) : this(
    message,
    ChatType.byId(type)
  )

  constructor(buf: PacketBuffer) : this(
    buf.readComponent(),
    buf.readByte()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeComponent(message)
    buf.writeByte(type.id.toInt())
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }

  override fun canSend(player: Player): Boolean {
    return when (player.chatVisibility) {
      ChatVisibility.HIDDEN -> false
      ChatVisibility.SYSTEM -> isChat
      else -> true
    }
  }
}
