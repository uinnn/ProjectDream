package dream.packet.game

import dream.entity.*
import dream.entity.base.EntityLiving
import dream.network.PacketBuffer
import dream.utils.and

/**
 * Serverbound packet spawn mob.
 *
 * Used to spawn a mob to a player.
 */
data class SPacketSpawnMob(
  var entityId: Int,
  var type: Int,
  var x: Int,
  var y: Int,
  var z: Int,
  var yaw: Byte,
  var pitch: Byte,
  var headPitch: Byte,
  var speedX: Short,
  var speedY: Short,
  var speedZ: Short,
  var watcher: Watcher? = null,
  protected var values: List<WatcherValue>? = null,
) : ServerGamePacket {

  /**
   * Gets the watcher values of this packet.
   */
  val watcherValues: List<WatcherValue>
    get() {
      if (values == null) {
        values = watcher?.allWatched
      }

      return values ?: emptyList()
    }

  constructor(entity: EntityLiving) : this(
    entityId = entity.serialId,
    type = entity.typeId,
    x = posToPacket(entity.x),
    y = posToPacket(entity.y),
    z = posToPacket(entity.z),
    yaw = rotationToPacketByte(entity.yaw),
    pitch = rotationToPacketByte(entity.pitch),
    headPitch = rotationToPacketByte(entity.headYaw),
    speedX = checkSpeed(entity.motionX).toShort(),
    speedY = checkSpeed(entity.motionY).toShort(),
    speedZ = checkSpeed(entity.motionZ).toShort(),
    watcher = entity.watcher
  )

  constructor(buf: PacketBuffer) : this(
    entityId = buf.readVarInt(),
    type = (buf.readByte() and 255).toInt(),
    x = buf.readInt(),
    y = buf.readInt(),
    z = buf.readInt(),
    yaw = buf.readByte(),
    pitch = buf.readByte(),
    headPitch = buf.readByte(),
    speedX = buf.readShort(),
    speedY = buf.readShort(),
    speedZ = buf.readShort(),
    watcher = null,
    values = Watcher.readObjects(buf)
  )

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(type and 255)
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeByte(yaw.toInt())
    buf.writeByte(pitch.toInt())
    buf.writeByte(headPitch.toInt())
    buf.writeShort(speedX.toInt())
    buf.writeShort(speedY.toInt())
    buf.writeShort(speedZ.toInt())
    watcher?.write(buf)
  }

  override fun process(handler: GamePacketHandler) {

  }
}
