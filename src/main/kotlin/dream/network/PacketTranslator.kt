package dream.network

import io.netty.buffer.*
import io.netty.channel.*
import javax.crypto.*

/**
 * Represents a translator for packets.
 */
open class PacketTranslator(val cipher: Cipher) {
   var input = ByteArray(0)
   var output = ByteArray(0)
   
   /**
    * Updates [input] property reading all bytes of [buffer] and
    * allocating in [input].
    *
    * @return [input]
    */
   fun update(buffer: ByteBuf): ByteArray {
      val size = buffer.readableBytes()
      if (input.size < size)
         input = ByteArray(size)
      
      buffer.readBytes(input, 0, size)
      return input
   }
   
   /**
    * Decrypts the cipher with given values.
    */
   fun decipher(context: ChannelHandlerContext, buf: ByteBuf): ByteBuf {
      val size = buf.readableBytes()
      val buffer = context.alloc().heapBuffer(cipher.getOutputSize(size))
      buffer.writerIndex(cipher.update(update(buf), 0, size, buffer.array(), buffer.arrayOffset()))
      return buffer
   }
   
   /**
    * Encrypts the cipher with the given values.
    */
   fun cipher(inp: ByteBuf, out: ByteBuf) {
      val size = inp.readableBytes()
      val outSize = cipher.getOutputSize(size)
      if (output.size < outSize)
         output = ByteArray(outSize)
      
      out.writeBytes(output, 0, cipher.update(update(inp), 0, size, output))
   }
}
