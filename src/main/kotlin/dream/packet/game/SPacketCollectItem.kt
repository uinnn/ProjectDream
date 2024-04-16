package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Serverbound Collect item packet.
 *
 * Called when collector collects an item on the ground.
 */
data class SPacketCollectItem(var itemId: Int, var collectorId: Int) : ServerGamePacket {

  constructor(item: Entity, collector: Entity) : this(item.serialId, collector.serialId)

  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readVarInt()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(itemId)
    buf.writeVarInt(collectorId)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
