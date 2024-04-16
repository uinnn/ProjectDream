package dream.packet.game

import dream.entity.base.Entity
import dream.network.PacketBuffer

/**
 * Clientbound Keep alive connection packet.
 */
data class CPacketKeepAlive(var id: Int) : ClientGamePacket {

  constructor(entity: Entity) : this(entity.serialId)

  constructor(buf: PacketBuffer) : this(buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(id)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleKeepAlive(this)
  }
}

