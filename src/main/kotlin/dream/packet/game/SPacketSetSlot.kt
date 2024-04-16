package dream.packet.game

import dream.item.*
import dream.network.*

data class SPacketSetSlot(var windowId: Int, var slot: Int, var item: ItemStack) : ServerGamePacket {
  
  init {
    // defensive copy
    item = item.copy()
  }
  
  constructor(buf: PacketBuffer) : this(
    windowId = buf.readByte().toInt(),
    slot = buf.readShort().toInt(),
    item = buf.readItem()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
    buf.writeShort(slot)
    buf.writeItem(item)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
