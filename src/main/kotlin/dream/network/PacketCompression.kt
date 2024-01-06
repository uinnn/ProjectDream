package dream.network

import dream.errors.*
import io.netty.buffer.*
import io.netty.channel.*
import io.netty.handler.codec.*
import java.util.zip.*

/**
 * Represents a decompressor for packet.
 */
open class PacketDecompressor(var threshold: Int) : ByteToMessageDecoder() {

  /**
   * The inflater used to decompress.
   */
  val inflater = Inflater()

  override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
    if (buf.readableBytes() == 0)
      return

    val buffer = PacketBuffer(buf)
    val size = buffer.readVarInt()

    if (size == 0) {
      out += buffer.readBytes(buffer.readableBytes())
    } else {
      if (size < threshold) {
        throw PacketCompressionException(
          "Badly compressed packet. Size of $size is below server threshold of $threshold"
        )
      }

      val input = ByteArray(buffer.readableBytes())
      buffer.readBytes(input)
      inflater.setInput(input)

      val result = ByteArray(size)
      inflater.inflate(result)
      out += result
      inflater.reset()
    }
  }

}

/**
 * Represents a compressor for packet.
 */
open class PacketCompressor(var threshold: Int) : MessageToByteEncoder<ByteBuf>() {

  /**
   * The buffer used to compress.
   */
  val buffer = ByteArray(8192)

  /**
   * The deflater used to compress.
   */
  val deflater = Deflater()

  override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
    val size = msg.readableBytes()
    val buf = PacketBuffer(out)

    if (size < threshold) {
      buf.writeVarInt(0)
      buf.writeBytes(msg)
    } else {
      val input = ByteArray(size)
      msg.readBytes(input)
      buf.writeVarInt(input.size)
      deflater.setInput(input)
      deflater.finish()

      while (!deflater.finished()) {
        val bytes = deflater.deflate(buffer)
        buf.writeBytes(buffer, 0, bytes)
      }

      deflater.reset()
    }
  }
}
