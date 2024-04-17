package dream.packet.game

import dream.network.*

data class SPacketEntityMove(
  var entityId: Int,
  var x: Byte = 0,
  var y: Byte = 0,
  var z: Byte = 0,
  var yaw: Byte = 0,
  var pitch: Byte = 0,
  var onGround: Boolean = false,
  var isRotating: Boolean = false
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
  }
}
