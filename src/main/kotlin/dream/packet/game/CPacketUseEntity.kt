package dream.packet.game

import dream.entity.base.*
import dream.level.*
import dream.network.*
import dream.pos.*

/**
 * Clientbound packet use entity.
 */
data class CPacketUseEntity(var id: Int, var action: EntityUse, var hit: Pos? = null) : ClientGamePacket {

  /**
   * Gets if this packet uses hit data.
   */
  val useHit: Boolean get() = action == EntityUse.INTERACT_AT

  /**
   * Gets if this packet has a hit data setted.
   */
  val hasHit: Boolean get() = hit != null

  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readEnum()) {
    if (useHit) {
      hit = buf.readVec()
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(id)
    buf.writeEnum(action)
    if (useHit) {
      buf.writeVec(hit ?: Pos.ZERO)
    }
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleUseEntity(this)
  }

  /**
   * Gets the entity associated by this packet.
   */
  fun entity(level: Level): Entity? {
    return level.getEntity(id)
  }
}

/**
 * All use actions that an entity can make in other entity.
 *
 * Used on [CPacketUseEntity].
 */
enum class EntityUse {
  INTERACT,
  ATTACK,
  INTERACT_AT
}
