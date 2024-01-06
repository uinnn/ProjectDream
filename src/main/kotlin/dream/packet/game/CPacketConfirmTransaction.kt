package dream.packet.game

import dream.network.*

/**
 * Clientbound packet confirm transaction.
 */
class CPacketConfirmTransaction(
  var id: Int,
  var uniqueId: Short,
  var accepted: Boolean,
) : ClientGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readByte().toInt(),
    buf.readShort(),
    buf.readBoolean()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeByte(id)
    buf.writeShort(uniqueId.toInt())
    buf.writeBoolean(accepted)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleConfirmTransaction(this)
  }
}
