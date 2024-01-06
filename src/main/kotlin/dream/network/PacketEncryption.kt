package dream.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.*
import javax.crypto.Cipher

/**
 * Encryptor for packets.
 *
 * Encrypt in Cipher cryptografy.
 */
class PacketEncryptor(val translator: PacketTranslator) : MessageToByteEncoder<ByteBuf>() {
  constructor(cipher: Cipher) : this(PacketTranslator(cipher))

  override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
    translator.cipher(msg, out)
  }
}

/**
 * Decryptor for packets.
 *
 * Decrypt in Cipher cryptografy.
 */
class PacketDecryptor(val translator: PacketTranslator) : MessageToMessageDecoder<ByteBuf>() {
  constructor(cipher: Cipher) : this(PacketTranslator(cipher))

  override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
    translator.decipher(ctx, msg)
  }
}
