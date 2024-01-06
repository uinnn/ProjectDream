package dream.packet.game

import dream.entity.player.*
import dream.network.*

/**
 * Clientbound packet settings.
 *
 * Called when a player changes their client configuration.
 *
 * Configurations:
 * * Language
 * * View Distance
 * * Chat Visibility
 * * Colors
 * * Skin Options
 */
class CPacketSettings(
  var language: String,
  var viewDistance: Int,
  var chatVisibility: ChatVisibility,
  var enableColors: Boolean,
  var skinOptions: Int,
) : ClientGamePacket {

  constructor(language: String, viewDistance: Int, chatId: Int, enableColors: Boolean, skinOptions: Int) : this(
    language,
    viewDistance,
    ChatVisibility.byId(chatId),
    enableColors,
    skinOptions
  )

  constructor(buf: PacketBuffer) : this(
    buf.readString(),
    buf.readByte().toInt(),
    buf.readByte().toInt(),
    buf.readBoolean(),
    buf.readUnsignedByte().toInt()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeString(language)
    buf.writeByte(viewDistance)
    buf.writeByte(chatVisibility.ordinal)
    buf.writeBoolean(enableColors)
    buf.writeByte(skinOptions)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleSettings(this)
  }
}
