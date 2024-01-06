package dream.packet.game

import dream.network.*

class SPacketPayload(var channel: String, var data: PacketBuffer) : ServerGamePacket {
  
  constructor(channel: String, data: ByteArray) : this(channel, PacketBuffer(data))
  
  constructor(buf: PacketBuffer) : this(buf.readString(), buf.readBuffer())
  
  override fun write(buf: PacketBuffer) {
    buf.writeString(channel)
    buf.writeBytes(data)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
