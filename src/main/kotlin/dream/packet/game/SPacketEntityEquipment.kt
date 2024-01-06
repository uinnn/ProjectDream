package dream.packet.game

import dream.item.*
import dream.network.*

class SPacketEntityEquipment(
  var entityId: Int,
  var slot: Int,
  var item: ItemStack,
) : ServerGamePacket {
  
  init {
    // defensive copy
    item = item.copy()
  }
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readShort().toInt(), buf.readItem())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeShort(slot)
    buf.writeItem(item)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
