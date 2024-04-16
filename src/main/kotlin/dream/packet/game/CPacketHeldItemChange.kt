package dream.packet.game

import dream.entity.player.Player
import dream.network.PacketBuffer

/**
 * Clientbound packet held item change.
 *
 * Called when a player changes the held item slot.
 */
data class CPacketHeldItemChange(var slot: Int) : ClientGamePacket {

  constructor(player: Player) : this(player.heldSlot)

  constructor(buf: PacketBuffer) : this(buf.readShort().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeShort(slot)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleHeldItemChange(this)
  }
}
