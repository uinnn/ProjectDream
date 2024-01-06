package dream.packet.game

import dream.network.*

class SPacketEntityLook(
  entityId: Int,
  yaw: Byte,
  pitch: Byte,
  onGround: Boolean = false,
) : SPacketEntityMove(entityId, yaw = yaw, pitch = pitch, onGround = onGround, isRotating = true) {
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte(),
    buf.readByte(),
    buf.readBoolean()
  )
  
  override fun write(buf: PacketBuffer) {
    super.write(buf)
    buf.writeByte(yaw)
    buf.writeByte(pitch)
    buf.writeBoolean(onGround)
  }
  
}
