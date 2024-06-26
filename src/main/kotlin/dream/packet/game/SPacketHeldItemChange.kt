package dream.packet.game

import dream.network.*

data class SPacketHeldItemChange(var slot: Int) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readByte().toInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(slot)
  }
}
