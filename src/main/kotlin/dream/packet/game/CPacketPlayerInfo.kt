package dream.packet.game

import dream.entity.base.Entity
import dream.network.PacketBuffer

/**
 * Clientbound packet player info.
 *
 * This is a base class for player move/look info.
 */
data class CPacketPlayerInfo(
  var x: Double = 0.0,
  var y: Double = 0.0,
  var z: Double = 0.0,
  var yaw: Float = 0f,
  var pitch: Float = 0f,
  var onGround: Boolean = false,
  var isMoving: Boolean = false,
  var isRotating: Boolean = false,
) : ClientGamePacket {

  constructor(ground: Boolean) : this(onGround = ground)

  constructor(buf: PacketBuffer) : this(buf.readBoolean())

  constructor(entity: Entity, onGround: Boolean, isMoving: Boolean, isRotating: Boolean) : this(
    entity.x,
    entity.y,
    entity.z,
    entity.yaw,
    entity.pitch,
    onGround,
    isMoving,
    isRotating
  )

  override fun write(buf: PacketBuffer) {
    buf.writeBoolean(onGround)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handlePlayerInfo(this)
  }
}
