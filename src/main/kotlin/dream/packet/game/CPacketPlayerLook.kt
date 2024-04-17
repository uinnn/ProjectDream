package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound packet player look.
 */
data class CPacketPlayerLook(
  override var yaw: Float,
  override var pitch: Float,
  override var onGround: Boolean,
) : CPacketPlayerMovement(yaw = yaw, pitch = pitch, onGround = onGround, isRotating = true) {

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
