package dream.packet.game

import dream.network.*

data class SPacketUpdateHealth(
  var health: Float,
  var food: Int,
  var saturation: Float
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readFloat(), buf.readVarInt(), buf.readFloat())
  
  override fun write(buf: PacketBuffer) {
    buf.writeFloat(health)
    buf.writeVarInt(food)
    buf.writeFloat(saturation)
  }
}
