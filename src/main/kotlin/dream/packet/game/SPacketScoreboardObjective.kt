package dream.packet.game

import dream.network.*

data class SPacketScoreboardObjective(
  var name: String,
  var mode: ScoreboardMode,
  var value: String,
  var render: ScoreboardRender,
) : ServerGamePacket {

  constructor(buf: PacketBuffer, name: String, mode: ScoreboardMode) : this(name, mode, "", ScoreboardRender.INTEGER) {
    if (mode.needsData) {
      value = buf.readString(32)
      render = ScoreboardRender.byName(buf.readString(16))
    }
  }

  constructor(buf: PacketBuffer) : this(buf, buf.readString(16), ScoreboardMode.byId(buf.readByte().toInt()))
  
  override fun write(buf: PacketBuffer) {
    buf.writeString(name)
    buf.writeByte(mode.ordinal)
    if (mode.needsData) {
      buf.writeString(value)
      buf.writeString(render.type)
    }
  }
}

/**
 * Represents the renderization types for scoreboard.
 *
 * Used in [SPacketScoreboardObjective].
 */
enum class ScoreboardRender(val type: String) {
  INTEGER("integer"),
  HEARTS("hearts");

  companion object {

    /**
     * Gets a [ScoreboardRender] by its name.
     */
    fun byName(name: String): ScoreboardRender {
      return when (name) {
        "hearts" -> HEARTS
        else -> INTEGER
      }
    }
  }
}

/**
 * Represents a mode for scoreboard display.
 *
 * Used in [SPacketScoreboardObjective].
 */
enum class ScoreboardMode(val needsData: Boolean) {
  CREATE(true),
  REMOVE(false),
  UPDATE(true);

  companion object {

    /**
     * Gets a [ScoreboardMode] by its id.
     */
    fun byId(id: Int): ScoreboardMode {
      return when (id) {
        1 -> REMOVE
        2 -> UPDATE
        else -> CREATE
      }
    }
  }
}
