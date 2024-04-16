package dream.packet.game

import dream.entity.base.*
import dream.network.*
import dream.utils.*

/**
 * Serverbound packet spawn object.
 */
data class SPacketSpawnObject(
  var entityId: Int,
  var type: Int,
  var x: Int,
  var y: Int,
  var z: Int,
  var yaw: Int,
  var pitch: Int,
  var hasVelocity: Int = 0,
  var speedX: Int = 0,
  var speedY: Int = 0,
  var speedZ: Int = 0,
) : ServerGamePacket {

  constructor(entity: Entity, type: Int, hasVelocity: Int = 0) : this(
    entity.serialId,
    type,
    posToPacket(entity.x),
    posToPacket(entity.y),
    posToPacket(entity.z),
    rotationToPacket(entity.yaw),
    rotationToPacket(entity.pitch),
    hasVelocity,
    checkSpeed(entity.motionX, hasVelocity),
    checkSpeed(entity.motionY, hasVelocity),
    checkSpeed(entity.motionZ, hasVelocity)
  )

  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte().toInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readByte().toInt(),
    buf.readByte().toInt(),
    buf.readInt()
  ) {
    if (hasVelocity > 0) {
      speedX = buf.readShort().toInt()
      speedY = buf.readShort().toInt()
      speedZ = buf.readShort().toInt()
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(type)
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeByte(yaw)
    buf.writeByte(pitch)
    buf.writeInt(hasVelocity)
    if (hasVelocity > 0) {
      buf.writeShort(speedX)
      buf.writeShort(speedY)
      buf.writeShort(speedZ)
    }
  }

  override fun process(handler: GamePacketHandler) {

  }
}

/**
 * Checks the [motion] as safely converted.
 */
internal fun checkSpeed(motion: Double): Int {
  return (between(-3.9, motion, 3.9) * 8000).toInt()
}

/**
 * Checks the [motion] as safely converted.
 */
internal fun checkSpeed(motion: Double, hasVelocity: Int): Int {
  return if (hasVelocity <= 0) 0 else checkSpeed(motion)
}
