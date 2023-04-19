package dream.packet.game

import dream.network.*

/**
 * Clientbound packet enchant item.
 */
class ClientPacketEnchantItem(var id: Int, var button: Int) : ClientGamePacket {
   
   constructor(buf: PacketBuffer) : this(buf.readByte().toInt(), buf.readByte().toInt())
   
   override fun write(buf: PacketBuffer) {
      buf.writeByte(id)
      buf.writeByte(button)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}
