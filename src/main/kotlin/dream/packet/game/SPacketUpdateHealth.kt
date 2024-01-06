package dream.packet.game

import dream.network.*

class SPacketUpdateHealth(
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
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
