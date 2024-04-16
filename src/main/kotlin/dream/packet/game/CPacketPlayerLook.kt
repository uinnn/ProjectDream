package dream.packet.game

import dream.entity.base.Entity
import dream.network.PacketBuffer

/**
 * Clientbound packet player look.
 */
class CPacketPlayerLook(
  yaw: Float,
  pitch: Float,
  ground: Boolean,
) : CPacketPlayerInfo(yaw = yaw, pitch = pitch, onGround = ground, isRotating = true) {

  constructor(entity: Entity) : this(entity.yaw, entity.pitch, entity.onGround)

  constructor(buf: PacketBuffer) : this(
    buf.readFloat(),
    buf.readFloat(),
    buf.readBoolean()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeFloat(yaw)
    buf.writeFloat(pitch)
    super.write(buf)
  }
}
