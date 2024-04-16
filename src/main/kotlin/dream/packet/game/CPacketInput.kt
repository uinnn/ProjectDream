package dream.packet.game

import dream.network.PacketBuffer
import dream.utils.hasBits
import dream.utils.setBitsIf

/**
 * Clientbound packet input.
 */
data class CPacketInput(
  var strafeSpeed: Float,
  var forwardSpeed: Float,
  var isJumping: Boolean,
  var isSneaking: Boolean,
) : ClientGamePacket {

  /**
   * Gets the raw data used to write [isJumping] and [isSneaking].
   */
  val data: Int
    get() = 0
      .setBitsIf(1, isJumping)
      .setBitsIf(2, isSneaking)

  constructor(strafeSpeed: Float, forwardSpeed: Float, data: Byte) : this(
    strafeSpeed,
    forwardSpeed,
    data.hasBits(1),
    data.hasBits(2)
  )

  constructor(buf: PacketBuffer) : this(
    buf.readFloat(),
    buf.readFloat(),
    buf.readByte()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeFloat(strafeSpeed)
    buf.writeFloat(forwardSpeed)
    buf.writeByte(data)
  }

  override fun process(handler: GamePacketHandler) {
    handler.handleInput(this)
  }
}
