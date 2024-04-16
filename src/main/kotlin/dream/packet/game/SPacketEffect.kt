package dream.packet.game

import dream.network.PacketBuffer
import dream.pos.Pos

data class SPacketEffect(
  var type: Int,
  var pos: Pos,
  var data: Int,
  var relativeVolume: Boolean
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readInt(), buf.readPos(), buf.readInt(), buf.readBoolean())

  override fun write(buf: PacketBuffer) {
    buf.writeInt(type)
    buf.writePos(pos)
    buf.writeInt(data)
    buf.writeBoolean(relativeVolume)
  }

  override fun process(handler: GamePacketHandler) {
  }
}
