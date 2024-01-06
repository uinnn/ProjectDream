package dream.packet.game

import dream.entity.base.*
import dream.level.*
import dream.network.*
import java.util.*

/**
 * Clientbound packet spectate.
 */
class CPacketSpectate(var id: UUID) : ClientGamePacket {

  constructor(entity: Entity) : this(entity.id)

  constructor(buf: PacketBuffer) : this(buf.readUUID())

  override fun write(buf: PacketBuffer) {
    buf.writeUUID(id)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleSpectate(this)
  }

  /**
   * Gets the entity associated to this packet.
   */
  fun entity(level: Level): Entity? {
    return level.getEntity(id)
  }
}
