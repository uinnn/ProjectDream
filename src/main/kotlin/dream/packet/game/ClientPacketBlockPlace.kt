package dream.packet.game

import dream.item.*
import dream.network.*
import dream.pos.*

/**
 * Clientbound packet block place.
 *
 * Called when player places a block.
 */
class ClientPacketBlockPlace(
   var pos: Pos,
   var placedDirection: Int,
   var item: ItemStack,
   var directionX: Float = 0f,
   var directionY: Float = 0f,
   var directionZ: Float = 0f,
) : ClientGamePacket {
   
   constructor(item: ItemStack) : this(EMPTY, 255, item)
   
   constructor(buf: PacketBuffer) : this(
      buf.readPos(),
      buf.readUnsignedByte().toInt(),
      buf.readItem(),
      buf.readUnsignedByte() / 16f,
      buf.readUnsignedByte() / 16f,
      buf.readUnsignedByte() / 16f
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writePos(pos)
      buf.writeByte(placedDirection)
      buf.writeItem(item)
      buf.writeByte((directionX * 16).toInt())
      buf.writeByte((directionY * 16).toInt())
      buf.writeByte((directionZ * 16).toInt())
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
   
   companion object {
      
      /**
       * Empty pos.
       */
      val EMPTY = Pos(-1, -1, -1)
   }
}
