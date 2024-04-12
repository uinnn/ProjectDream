package dream.packet.game

import dream.chat.Component
import dream.network.PacketBuffer

/**
 * Serverbound packet title.
 */
class SPacketTitle(
  var type: TitleType,
  var text: Component = Component.EMPTY,
  var fadeIn: Int = -1,
  var stay: Int = -1,
  var fadeOut: Int = -1
) : ServerGamePacket {

  constructor(fadeIn: Int, stay: Int, fadeOut: Int) : this(TitleType.TIMES, fadeIn = fadeIn, stay = stay, fadeOut = fadeOut)

  constructor(buf: PacketBuffer) : this(buf.readEnum<TitleType>()) {
    if (type == TitleType.TITLE || type == TitleType.SUBTITLE) {
      text = buf.readComponent()
    }

    if (type == TitleType.TIMES) {
      fadeIn = buf.readInt()
      stay = buf.readInt()
      fadeOut = buf.readInt()
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeEnum(type)
    if (type == TitleType.TITLE || type == TitleType.SUBTITLE) {
      buf.writeComponent(text)
    }

    if (type == TitleType.TIMES) {
      buf.writeInt(fadeIn)
      buf.writeInt(stay)
      buf.writeInt(fadeOut)
    }
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

/**
 * All title types for [SPacketTitle].
 */
enum class TitleType {
  TITLE,
  SUBTITLE,
  TIMES,
  CLEAR,
  RESET;

  companion object {

    /**
     * Gets a title type by their name.
     */
    fun byName(name: String): TitleType {
      return try {
        valueOf(name.uppercase())
      } catch (e: Exception) {
        TITLE
      }
    }
  }
}
