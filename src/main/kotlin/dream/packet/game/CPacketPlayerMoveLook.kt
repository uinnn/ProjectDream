package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound packet player movement look.
 */
data class CPacketPlayerMoveLook(
  override var x: Double,
  override var y: Double,
  override var z: Double,
  override var yaw: Float,
  override var pitch: Float,
  override var onGround: Boolean,
) : CPacketPlayerMovement(x, y, z, yaw, pitch, onGround, true, true) {

  constructor(entity: Entity) : this(
    entity.x,
    entity.y,
    entity.z,
    entity.yaw,
    entity.pitch,
    entity.onGround
  )

  constructor(buf: PacketBuffer) : this(
    buf.readDouble(),
    buf.readDouble(),
    buf.readDouble(),
    buf.readFloat(),
    buf.readFloat(),
    buf.readBoolean()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeDouble(x)
    buf.writeDouble(y)
    buf.writeDouble(z)
    buf.writeFloat(yaw)
    buf.writeFloat(pitch)
    super.write(buf)
  }
}
