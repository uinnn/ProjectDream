package dream.packet.game

import dream.network.*

data class SPacketExplosion(
  var x: Double,
  var y: Double,
  var z: Double,
  var radius: Double,
  var records: MutableList<ExplosionRecord>,
  var motionX: Float,
  var motionY: Float,
  var motionZ: Float
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readFloat().toDouble(),
    buf.readFloat().toDouble(),
    buf.readFloat().toDouble(),
    buf.readFloat().toDouble(),
    buf.readList(ArrayList(), buf.readInt()) { ExplosionRecord(it.readByte(), it.readByte(), it.readByte()) },
    buf.readFloat(),
    buf.readFloat(),
    buf.readFloat()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeFloat(x)
    buf.writeFloat(y)
    buf.writeFloat(z)
    buf.writeFloat(radius)
    buf.writeInt(records.size)
    for (record in records) {
      buf.writeByte(record.x)
      buf.writeByte(record.y)
      buf.writeByte(record.z)
    }
    buf.writeFloat(motionX)
    buf.writeFloat(motionY)
    buf.writeFloat(motionZ)
  }

}

/**
 * A record for explosion. This is equals to the blocks affected by an explosion.
 *
 * Used in [SPacketExplosion].
 */
data class ExplosionRecord(var x: Byte, var y: Byte, var z: Byte)
