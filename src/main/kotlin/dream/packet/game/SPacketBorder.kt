package dream.packet.game

import dream.network.*

data class SPacketBorder(
  var action: BorderAction,
  var size: Int = -1,
  var centerX: Double = -1.0,
  var centerZ: Double = -1.0,
  var targetSize: Double = -1.0,
  var diameter: Double = -1.0,
  var speed: Long = -1,
  var warningTime: Int = -1,
  var warningBlocks: Int = -1
) : ServerGamePacket {

  constructor(buf: PacketBuffer, action: BorderAction) : this(action) {
    when (action) {
      BorderAction.SET_SIZE -> targetSize = buf.readDouble()
      BorderAction.LERP_SIZE -> {
        diameter = buf.readDouble()
        targetSize = buf.readDouble()
        speed = buf.readVarLong()
      }

      BorderAction.SET_CENTER -> {
        centerX = buf.readDouble()
        centerZ = buf.readDouble()
      }

      BorderAction.SET_WARNING_TIME -> warningTime = buf.readVarInt()
      BorderAction.SET_WARNING_BLOCKS -> warningBlocks = buf.readVarInt()

      BorderAction.INITIALIZE -> {
        centerX = buf.readDouble()
        centerZ = buf.readDouble()
        diameter = buf.readDouble()
        targetSize = buf.readDouble()
        speed = buf.readVarLong()
        size = buf.readVarInt()
        warningTime = buf.readVarInt()
        warningBlocks = buf.readVarInt()
      }
    }
  }

  constructor(buf: PacketBuffer) : this(buf, buf.readEnum())

  override fun write(buf: PacketBuffer) {
    buf.writeEnum(action)
    when (action) {
      BorderAction.SET_SIZE -> buf.writeDouble(targetSize)
      BorderAction.LERP_SIZE -> {
        buf.writeDouble(diameter)
        buf.writeDouble(targetSize)
        buf.writeVarLong(speed)
      }

      BorderAction.SET_CENTER -> {
        buf.writeDouble(centerX)
        buf.writeDouble(centerZ)
      }

      BorderAction.SET_WARNING_TIME -> buf.writeVarInt(warningTime)
      BorderAction.SET_WARNING_BLOCKS -> buf.writeVarInt(warningBlocks)

      BorderAction.INITIALIZE -> {
        buf.writeDouble(centerX)
        buf.writeDouble(centerZ)
        buf.writeDouble(diameter)
        buf.writeDouble(targetSize)
        buf.writeVarLong(speed)
        buf.writeVarInt(size)
        buf.writeVarInt(warningTime)
        buf.writeVarInt(warningBlocks)
      }
    }
  }
}

/**
 * Represents all actions that a border can perform.
 *
 * Used in [SPacketBorder].
 */
enum class BorderAction {
  SET_SIZE,
  LERP_SIZE,
  SET_CENTER,
  INITIALIZE,
  SET_WARNING_TIME,
  SET_WARNING_BLOCKS
}
