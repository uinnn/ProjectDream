package dream.packet.game

import dream.network.*

/**
 * Clientbound packet status.
 */
class CPacketStatus(var status: PlayerStatus) : ClientGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readEnum<PlayerStatus>())

  override fun write(buf: PacketBuffer) {
    buf.writeEnum(status)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleStatus(this)
  }
}

/**
 * All status performed by player.
 *
 * Used on [CPacketStatus].
 */
enum class PlayerStatus {
  PERFORM_RESPAWN,
  REQUEST_STATS,
  OPEN_INVENTORY_ACHIEVEMENT;
}
