package dream.packet.game

import dream.network.*

data class SPacketChangeGameState(var state: Int, var value: Float) : ServerGamePacket {
  
  constructor(state: GameStates, value: Float) : this(state.id, value)
  
  constructor(buf: PacketBuffer) : this(buf.readUnsignedByte().toInt(), buf.readFloat())
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(state)
    buf.writeFloat(value)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

/**
 * Enum class representing different game states.
 *
 * @param id the unique ID of the game state.
 */
enum class GameStates(val id: Int) {
  START_RAIN(1),
  END_RAIN(2),
  CHANGE_GAMEMODE(3),
  ENTER_CREDITS(4),
  DEMO_MESSAGE(5),
  HIT(6),
  RAIN_STRENGTH(7),
  THUNDER_STRENGTH(8),
  GUARDIAN_APPEARANCE(10);
}
