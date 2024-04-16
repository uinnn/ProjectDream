package dream.packet.login

import dream.network.*

/**
 * Serverbound Response for enabling compression packet.
 */
data class SPacketEnableCompression(var threshold: Int) : ServerLoginPacket {

  constructor(buf: PacketBuffer) : this(buf.readVarInt())

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(threshold)
  }

  override fun process(handler: LoginPacketHandler) {
  }
}
