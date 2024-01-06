package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound packet player movement look.
 */
class CPacketPlayerMoveLook(
  x: Double,
  y: Double,
  z: Double,
  yaw: Float,
  pitch: Float,
  ground: Boolean,
) : CPacketPlayerInfo(x, y, z, yaw, pitch, ground, true, true) {

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
