package dream.packet.login

import dream.misc.decodePublicKey
import dream.network.PacketBuffer
import java.security.PublicKey

/**
 * Serverbound Request for encryption response packet.
 */
class SPacketEncryptionRequest(
  var hashedServerId: String,
  var key: PublicKey,
  var verifyToken: ByteArray,
) : ServerLoginPacket {

  constructor(buf: PacketBuffer) : this(
    buf.readString(),
    buf.readByteArray().decodePublicKey(),
    buf.readByteArray()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeString(hashedServerId)
    buf.writeByteArray(key.encoded)
    buf.writeByteArray(verifyToken)
  }

  override fun process(handler: LoginPacketHandler) {
  }
}
