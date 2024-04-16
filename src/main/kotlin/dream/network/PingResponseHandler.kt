package dream.network

import dream.misc.Open
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.net.InetSocketAddress

/**
 * Represents a handler for ping response.
 */
@Open
class PingResponseHandler(val network: NetworkSystem) : ChannelInboundHandlerAdapter() {

  override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
    println("channel: ${ctx.channel()}")

    val buffer = msg as ByteBuf
    buffer.markReaderIndex()
    var reset = true

    try {
      if (buffer.readUnsignedByte() == 254.toShort()) {
        val address = ctx.channel().remoteAddress() as InetSocketAddress
        //val server = network.server

        when (buffer.readableBytes()) {
          // minecraft version 1.3 or below
          0 -> {
            println("Ping from 1.3 or below at ${address.address}")
            val format = String.format("%s\u00a7%d\u00a7%d", "Hello World!", 0, 3)
            writeAndFlush(ctx, getStringBuffer(format))
          }

          // minecraft version 1.4-1.5
          1 -> {
            if (buffer.readUnsignedByte() != 1.toShort())
              return

            println("Ping from 1.4-1.5 at ${address.address}")
            writeAndFlush(ctx, getStringBuffer(""))
          }

          // minecraft version 1.6 or higher
          else -> {
            if (!buffer.result())
              return

            println("Ping from 1.6 or higher at ${address.address}")
            val format =
              String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, "1.8.9", "Hello World!", 0, 3)
            val stringBuffer = getStringBuffer(format)
            try {
              writeAndFlush(ctx, stringBuffer)
            } finally {
              stringBuffer.release()
            }
          }
        }

        buffer.release()
        reset = false
        return
      }
    } catch (ex: RuntimeException) {
      return
    } finally {
      if (reset) {
        buffer.resetReaderIndex()
        ctx.channel().pipeline().remove("legacy_query")
        ctx.fireChannelRead(msg)
      }
    }
  }

  /**
   * Writes and flush [data] in [context].
   */
  fun writeAndFlush(context: ChannelHandlerContext, data: ByteBuf) {
    context.pipeline().firstContext().writeAndFlush(data).addListener(ChannelFutureListener.CLOSE)
  }

  /**
   * Gets a new [ByteBuf] with data from [data].
   */
  fun getStringBuffer(data: String): ByteBuf = Unpooled.buffer().apply {
    val chars = data.toCharArray()
    writeByte(255)
    writeShort(chars.size)
    for (char in chars)
      writeChar(char.code)
  }

  /**
   * Gets the result of encoded data for minecraft versions 1.6+
   */
  fun ByteBuf.result(): Boolean {
    var flag1 = readUnsignedByte().toInt() == 1
    flag1 = flag1 and (readUnsignedByte().toInt() == 250)
    flag1 = flag1 and ("MC|PingHost" == String(readBytes(readShort() * 2).array(), Charsets.UTF_16BE))
    val j: Int = readUnsignedShort()
    flag1 = flag1 and (readUnsignedByte() >= 73)
    flag1 = flag1 and (3 + readBytes(readShort() * 2).array().size + 4 == j)
    flag1 = flag1 and (readInt() <= 65535)
    flag1 = flag1 and (readableBytes() == 0)
    return flag1

    /*var base = readUnsignedByte() == 1.toShort()
    base = base and (readUnsignedByte() == 250.toShort())
    base = base and (String(readBytes(readShort() * 2).array(), Charsets.UTF_16BE) == "MC|PingHost")
    val max = readUnsignedShort()
    base = base and (readUnsignedByte() >= 73)
    base = base and (7 + readBytes(readShort() * 2).array().size == max)
    base = base and (readInt() <= 65535)
    base = base and (readableBytes() == 0)
    return base*/
  }
}
