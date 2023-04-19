package dream.packet.game

import dream.item.*
import dream.network.*

/**
 * Clientbound packet window creative action.
 */
class ClientPacketCreativeAction(
   var slot: Int,
   var item: ItemStack,
) : ClientGamePacket {
   
   constructor(buf: PacketBuffer) : this(buf.readShort().toInt(), buf.readItem())
   
   override fun write(buf: PacketBuffer) {
      buf.writeShort(slot)
      buf.writeItem(item)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}
