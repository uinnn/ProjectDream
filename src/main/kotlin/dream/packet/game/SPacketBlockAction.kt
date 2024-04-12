package dream.packet.game

import dream.block.Block
import dream.network.PacketBuffer
import dream.pos.Pos

/**
 * Serverbound block action packet.
 */
class SPacketBlockAction(
  var pos: Pos,
  var instrument: Int,
  var pitch: Int,
  var block: Block
) : ServerGamePacket {

  constructor(buf: PacketBuffer) : this(
    buf.readPos(), buf.readUnsignedByte().toInt(), buf.readUnsignedByte().toInt(), buf.readBlock()
  )

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeByte(instrument)
    buf.writeByte(pitch)
    buf.writeBlock(block)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
