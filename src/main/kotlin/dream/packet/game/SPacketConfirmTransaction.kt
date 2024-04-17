package dream.packet.game

import dream.network.*

data class SPacketConfirmTransaction(
  var windowId: Int,
  var actionId: Short,
  var accepted: Boolean
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readUnsignedByte().toInt(), buf.readShort(), buf.readBoolean())

  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
    buf.writeShort(actionId)
    buf.writeBoolean(accepted)
  }
}
