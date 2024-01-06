package dream.packet.game

import dream.network.*

class SPacketEntityMoveLook(
  entityId: Int,
  x: Byte,
  y: Byte,
  z: Byte,
  yaw: Byte,
  pitch: Byte,
  onGround: Boolean = false,
) : SPacketEntityMove(entityId, x, y, z, yaw, pitch, onGround, true) {
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte(),
    buf.readByte(),
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
    buf.writeByte(yaw)
    buf.writeByte(pitch)
    buf.writeBoolean(onGround)
  }
  
}

