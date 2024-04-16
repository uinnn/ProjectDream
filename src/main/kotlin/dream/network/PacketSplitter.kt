package dream.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToByteEncoder

/**
 * Represents a packet decoder used in connections splitter functions.
 */
class PacketSplitter : ByteToMessageDecoder() {
  override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
    buf.markReaderIndex()
    val array = ByteArray(3)

    for (i in 0 until 3) {
      if (!buf.isReadable) {
        buf.resetReaderIndex()
        return
      }

      array[i] = buf.readByte()

      if (array[i] >= 0) {
        val buffer = PacketBuffer(array)

        try {
          val size = buffer.readVarInt()
          if (buf.readableBytes() >= size) {
            out += buf.readBytes(size)
            return
          }

          buf.resetReaderIndex()
        } finally {
          buffer.release()
        }

        return
      }
    }

    error("Corrupted packet. Lenght wider than 21 bits")
  }
}

/**
 * Represents a packet encoder used in connections prepender functions.
 */
class PacketPrepender : MessageToByteEncoder<ByteBuf>() {
  override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
    val bytes = msg.readableBytes()
    val size = varInt(bytes)

    if (size > 3) {
      error("Unable to fit $bytes bytes into 3 bytes")
    }

    val buffer = PacketBuffer(out)
    buffer.ensureWritable(size + bytes)
    buffer.writeVarInt(bytes)
    buffer.writeBytes(msg, msg.readerIndex(), bytes)
  }
}
