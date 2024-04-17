package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Clientbound packet player movement.
 */
data class CPacketPlayerMove(
  override var x: Double,
  override var y: Double,
  override var z: Double,
  override var onGround: Boolean,
) : CPacketPlayerMovement(x, y, z, onGround = onGround, isMoving = true) {

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
