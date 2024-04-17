package dream.packet.game

import dream.item.*
import dream.network.*

data class SPacketWindowItems(
  var windowId: Int,
  var items: List<ItemStack>
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readByte().toInt(),
    buf.readList(ArrayList(), buf.readShort().toInt()) { it.readItem() }
  )

  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
    buf.writeShort(items.size)
    for (item in items) {
      buf.writeItem(item)
    }
  }
}
