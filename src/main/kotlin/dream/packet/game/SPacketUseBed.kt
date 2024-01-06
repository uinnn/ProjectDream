package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Serverbound Use bed packet.
 *
 * Called when player uses a bed.
 */
class SPacketUseBed(var entityId: Int) : ServerGamePacket {

  constructor(entity: Entity) : this(entity.serialId)

  constructor(buf: PacketBuffer) : this(buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
