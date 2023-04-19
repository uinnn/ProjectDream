package dream.packet.game

import dream.network.*
import dream.pos.*

/**
 * Clientbound packet player dig.
 *
 * Called when a player is digging a block.
 */
class ClientPacketDig(
   var action: DigAction,
   var pos: Pos,
   var direction: Direction,
) : ClientGamePacket {
   
   constructor(action: DigAction, pos: Pos, directionId: Int) : this(
      action,
      pos,
      Direction.front(directionId)
   )
   
   constructor(buf: PacketBuffer) : this(
      buf.readEnum(),
      buf.readPos(),
      buf.readUnsignedByte().toInt()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeEnum(action)
      buf.writePos(pos)
      buf.writeByte(direction.ordinal)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}

/**
 * All dig actions that a player can perform when digging.
 *
 * Used on [ClientPacketDig].
 */
enum class DigAction {
   START_DIG_BLOCK,
   ABORT_DIG_BLOCK,
   STOP_DIG_BLOCK,
   DROP_ALL_ITEMS,
   DROP_ITEM,
   RELEASE_USE_ITEM;
}
