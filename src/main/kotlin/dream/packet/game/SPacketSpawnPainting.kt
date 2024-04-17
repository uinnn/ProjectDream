package dream.packet.game

import dream.entity.item.*
import dream.network.*
import dream.pos.*

data class SPacketSpawnPainting(
  var entityId: Int,
  var pos: Pos,
  var facing: Direction,
  var art: PaintingArt
) : ServerGamePacket {
  
  constructor(painting: Painting) : this(painting.serialId, painting.hangingPos, painting.facing, painting.art)
  
  constructor(buf: PacketBuffer) : this(
    entityId = buf.readVarInt(),
    art = PaintingArt.byTitle(buf.readString()),
    pos = buf.readPos(),
    facing = Direction.horizontal(buf.readUnsignedByte().toInt())
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeString(art.title)
    buf.writePos(pos)
    buf.writeByte(facing.horizontalIndex)
  }
}
