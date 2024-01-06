package dream.network

import io.netty.buffer.*
import io.netty.channel.*
import io.netty.handler.codec.*

/**
 * Represents a packet decoder used in connections splitter functions.
 */
class PacketSplitter : ByteToMessageDecoder() {
  override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
    buf.markReaderIndex()
    val array = ByteArray(3)

    repeat(3) {
      if (!buf.isReadable) {
        buf.resetReaderIndex()
        return
      }

      array[it] = buf.readByte()
      if (array[it] >= 0) {
        val buffer = PacketBuffer(array)
        val size = buffer.readVarInt()
        if (buf.readableBytes() >= size) {
          out += buf.readBytes(size)
          return
        }

        buf.resetReaderIndex()
        buffer.release()
      }
    }
  }
}

/**
 * Represents a packet encoder used in connections prepender functions.
 */
class PacketPrepender : MessageToByteEncoder<ByteBuf>() {
  override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
    val bytes = msg.readableBytes()
    val size = varInt(bytes)
    PacketBuffer(out).apply {
      ensureWritable(size + bytes)
      writeVarInt(bytes)
      writeBytes(msg, msg.readerIndex(), bytes)
    }
  }
}
