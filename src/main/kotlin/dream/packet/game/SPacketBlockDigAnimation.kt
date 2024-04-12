package dream.packet.game

import dream.network.PacketBuffer
import dream.pos.Pos

/**
 * Serverbound packet for digging animation.
 */
class SPacketBlockDigAnimation(
  var breakerId: Int,
  var pos: Pos,
  var progress: Int
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readPos(), buf.readUnsignedByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(breakerId)
    buf.writePos(pos)
    buf.writeByte(progress)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }

}
