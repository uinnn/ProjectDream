package dream.packet.game

import dream.network.*

data class SPacketWindowProperty(
  var windowId: Int,
  var propertyId: Int,
  var value: Int
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readUnsignedByte().toInt(),
    buf.readShort().toInt(),
    buf.readShort().toInt()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
    buf.writeShort(propertyId)
    buf.writeShort(value)
  }
}
