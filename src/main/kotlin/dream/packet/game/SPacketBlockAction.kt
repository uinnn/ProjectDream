package dream.packet.game

import dream.block.*
import dream.network.*
import dream.pos.*

/**
 * Serverbound block action packet.
 */
data class SPacketBlockAction(
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
