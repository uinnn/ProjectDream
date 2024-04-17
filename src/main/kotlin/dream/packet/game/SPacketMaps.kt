package dream.packet.game

import dream.network.*
import dream.utils.*
import korlibs.io.stream.*

class SPacketMaps(
  var mapId: Int,
  var mapScale: Byte,
  var records: MutableList<MapRecord>,
  var minX: Int,
  var minY: Int = -1,
  var maxX: Int = -1,
  var maxY: Int = -1,
  var data: ByteArray = EMPTY_BYTE_ARRAY
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readByte(),
    buf.readList(ArrayList(), buf.readInt()) { MapRecord(it.readByte(), it.readByte(), it.readByte()) },
    buf.readUnsignedByte().toInt()
  ) {
    if (minX > 0) {
      minY = buf.readUnsignedByte().toInt()
      maxX = buf.readUnsignedByte().toInt()
      maxY = buf.readUnsignedByte().toInt()
      data = buf.readByteArray()
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(mapId)
    buf.writeByte(mapScale)
    buf.writeVarInt(records.size)
    for (record in records) {
      buf.writeByte(record.dirType)
      buf.writeByte(record.x)
      buf.writeByte(record.z)
    }
    buf.writeByte(minX)
    if (minX > 0) {
      buf.writeByte(minY)
      buf.writeByte(maxX)
      buf.writeByte(maxY)
      buf.writeByteArray(data)
    }
  }
}

/**
 * Represents a record for a map.
 *
 * Used in [SPacketMaps].
 */
data class MapRecord(var direction: Byte, var type: Byte, var x: Byte, var z: Byte) {
  val dirType get() = (direction and 15) shl 4 or (type and 15)

  constructor(dirType: Byte, x: Byte, z: Byte) : this(dirType shl 4 and 15, dirType and 15, x, z)
}
