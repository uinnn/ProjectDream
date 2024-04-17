package dream.packet.game

import dream.entity.base.*
import dream.network.*
import dream.utils.*

/**
 * Serverbound Spawns entity packet.
 */
data class SPacketSpawnEntity(
  var entityId: Int,
  var type: Int,
  var x: Int,
  var y: Int,
  var z: Int,
) : ServerGamePacket {

  constructor(entity: Entity) : this(
    entity.serialId,
    entity.typeId,
    posToPacket(entity.x),
    posToPacket(entity.y),
    posToPacket(entity.z)
  )

  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte().toInt(),
    buf.readInt(),
    buf.readInt(),
    buf.readInt()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(type)
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
  }
}

/**
 * Converts ``X/Y/Z`` [value] to be writed in packets.
 */
internal fun posToPacket(value: Double): Int {
  return floorInt(value * 32)
}

/**
 * Converts ``yaw/pitch`` [value] to be writed in packets.
 */
internal fun rotationToPacket(value: Float): Int {
  return floorInt(value * 256f / 360f)
}

/**
 * Converts ``yaw/pitch`` [value] to be writed in packets.
 */
internal fun rotationToPacketByte(value: Float): Byte {
  return (value * 256f / 360f).asByte()
}
