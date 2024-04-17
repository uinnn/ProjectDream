package dream.packet.game

import dream.network.*

class SPacketUpdateScore(
  var name: String,
  var action: UpdateScoreAction,
  var objective: String,
  var value: Int = 0,
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readString(40), buf.readEnum(), buf.readString(16)) {
    if (action == UpdateScoreAction.CHANGE) {
      value = buf.readVarInt()
    }
  }
  
  override fun write(buf: PacketBuffer) {
    buf.writeString(name)
    buf.writeEnum(action)
    buf.writeString(objective)
    if (action == UpdateScoreAction.CHANGE) {
      buf.writeVarInt(value)
    }
  }
}

enum class UpdateScoreAction {
  CHANGE,
  REMOVE
}
