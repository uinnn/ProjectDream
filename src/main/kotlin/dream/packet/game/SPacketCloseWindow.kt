package dream.packet.game

import dream.network.*

data class SPacketCloseWindow(var windowId: Int) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readUnsignedByte().toInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
  }
}
