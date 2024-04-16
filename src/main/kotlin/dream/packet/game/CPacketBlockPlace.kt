package dream.packet.game

import dream.item.ItemStack
import dream.network.PacketBuffer
import dream.pos.Pos

/**
 * Clientbound packet block place.
 *
 * Called when player places a block.
 */
data class CPacketBlockPlace(
  var pos: Pos,
  var placedDirection: Int,
  var item: ItemStack,
  var hitX: Float = 0f,
  var hitY: Float = 0f,
  var hitZ: Float = 0f,
) : ClientGamePacket {

  constructor(item: ItemStack) : this(EMPTY, 255, item)

  constructor(buf: PacketBuffer) : this(
    buf.readPos(),
    buf.readUnsignedByte().toInt(),
    buf.readItem(),
    buf.readUnsignedByte() / 16f,
    buf.readUnsignedByte() / 16f,
    buf.readUnsignedByte() / 16f
  )

  override fun write(buf: PacketBuffer) {
    buf.writePos(pos)
    buf.writeByte(placedDirection)
    buf.writeItem(item)
    buf.writeByte((hitX * 16).toInt())
    buf.writeByte((hitY * 16).toInt())
    buf.writeByte((hitZ * 16).toInt())
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleBlockPlace(this)
  }

  companion object {

    /**
     * Empty pos.
     */
    val EMPTY = Pos(-1, -1, -1)
  }
}
