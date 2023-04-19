package dream.packet.login

import dream.misc.*
import dream.network.*
import java.security.*
import javax.crypto.*

/**
 * Clientbound Response for login encryption packet.
 */
class ClientPacketEncryptionResponse(
   var keyEncrypted: ByteArray,
   var verifyTokenEncrypted: ByteArray,
) : ClientLoginPacket {
   
   constructor(buf: PacketBuffer) : this(
      buf.readByteArray(),
      buf.readByteArray()
   )
   
   constructor(key: SecretKey, publicKey: PublicKey, verifyToken: ByteArray) : this(
      key.encoded.cipher(publicKey),
      verifyToken.cipher(publicKey)
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeByteArray(keyEncrypted)
      buf.writeByteArray(verifyTokenEncrypted)
   }
   
   override fun process(handler: LoginPacketHandler) {
      handler.handleEncryptionResponse(this)
   }
}
