package dream.packet.game

import dream.network.*
import dream.pos.*
import dream.utils.*

data class SPacketPosLook(
  var x: Double,
  var y: Double,
  var z: Double,
  var yaw: Float,
  var pitch: Float,
  var flags: List<AxisLook>
) : ServerGamePacket {
  
  constructor(buf: PacketBuffer) : this(
    buf.readDouble(),
    buf.readDouble(),
    buf.readDouble(),
    buf.readFloat(),
    buf.readFloat(),
    buf.readUnsignedByte().getFlags()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeDouble(x)
    buf.writeDouble(y)
    buf.writeDouble(z)
    buf.writeFloat(yaw)
    buf.writeFloat(pitch)
    buf.writeByte(flags.masked())
  }
}
