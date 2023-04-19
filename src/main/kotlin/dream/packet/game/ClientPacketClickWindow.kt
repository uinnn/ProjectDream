package dream.packet.game

import dream.inventory.ClickMode
import dream.item.ItemStack
import dream.network.PacketBuffer

/**
 * Clientbound packet click window.
 *
 * Called when player clicks on an inventory.
 */
class ClientPacketClickWindow(
   var id: Int,
   var slot: Int,
   var keyboard: Int,
   var action: Short,
   var mode: ClickMode,
   var clickedItem: ItemStack,
) : ClientGamePacket {
   
   constructor(id: Int, slot: Int, keyboard: Int, action: Short, mode: Int, item: ItemStack)
         : this(id, slot, keyboard, action, ClickMode.byId(mode), item)
   
   constructor(buf: PacketBuffer) : this(
      buf.readByte().toInt(),
      buf.readShort().toInt(),
      buf.readByte().toInt(),
      buf.readShort(),
      buf.readByte().toInt(),
      buf.readItem()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeByte(id)
      buf.writeShort(slot)
      buf.writeByte(keyboard)
      buf.writeShort(action.toInt())
      buf.writeByte(mode.id)
      buf.writeItem(clickedItem)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}
