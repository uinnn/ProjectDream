package dream.packet.game

import dream.nbt.types.*
import dream.network.*
import dream.pos.*

data class SPacketUpdateTile(
  var pos: Pos,
  var metadata: Byte,
  var tag: CompoundTag
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readPos(), buf.readByte(), buf.readCompound())

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeByte(metadata)
    buf.writeCompound(tag)
  }
}
