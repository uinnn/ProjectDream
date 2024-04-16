package dream.packet.game

import dream.item.ItemStack
import dream.network.PacketBuffer

/**
 * Clientbound packet window creative action.
 */
data class CPacketCreativeAction(var slot: Int, var item: ItemStack) : ClientGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readShort().toInt(), buf.readItem())

  override fun write(buf: PacketBuffer) {
    buf.writeShort(slot)
    buf.writeItem(item)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleCreativeAction(this)
  }
}
