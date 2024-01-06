package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound Keep alive connection packet.
 */
class CPacketKeepAlive(var id: Int) : ClientGamePacket {

  constructor(entity: Entity) : this(entity.serialId)

  constructor(buf: PacketBuffer) : this(buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(id)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleKeepAlive(this)
  }
}

