package dream.packet.game

import dream.network.*

class SPacketEntityMovement(
  entityId: Int,
  x: Byte,
  y: Byte,
  z: Byte,
  onGround: Boolean = false,
) : SPacketEntityMove(entityId, x, y, z, onGround = onGround) {
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte(),
    buf.readByte(),
    buf.readByte(),
    buf.readBoolean()
  )
  
  override fun write(buf: PacketBuffer) {
    super.write(buf)
    buf.writeByte(x)
    buf.writeByte(y)
    buf.writeByte(z)
    buf.writeBoolean(onGround)
  }
  
}
