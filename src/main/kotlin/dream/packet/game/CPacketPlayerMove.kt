package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound packet player movement.
 */
class CPacketPlayerMove(
  x: Double,
  y: Double,
  z: Double,
  ground: Boolean,
) : CPacketPlayerInfo(x, y, z, onGround = ground, isMoving = true) {

  constructor(entity: Entity) : this(entity.x, entity.y, entity.z, entity.onGround)

  constructor(buf: PacketBuffer) : this(
    buf.readDouble(),
    buf.readDouble(),
    buf.readDouble(),
    buf.readBoolean()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeDouble(x)
    buf.writeDouble(y)
    buf.writeDouble(z)
    super.write(buf)
  }
}
