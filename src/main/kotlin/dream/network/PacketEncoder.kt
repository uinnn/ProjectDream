package dream.network

import dream.misc.Open
import dream.packet.*
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.*

/**
 * Represents a message encoder for packets.
 */
@Open
class PacketEncoder(val direction: PacketDirection) : MessageToByteEncoder<HandledPacket>() {
  override fun encode(ctx: ChannelHandlerContext, msg: Packet<PacketHandler>, out: ByteBuf) {
    val id = ctx.channel()
      .attr(NetworkManager.ATTRIBUTE_CONNECTION)
      .get()
      .id(direction, msg) ?: return

    PacketBuffer(out).apply {
      writeVarInt(id)
      msg.write(this)
    }
  }
}

/**
 * Represents a message decoder for packets.
 */
@Open
class PacketDecoder(val direction: PacketDirection) : ByteToMessageDecoder() {
  override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
    if (buf.readableBytes() == 0) {
      return
    }

    out += ctx.channel()
      .attr(NetworkManager.ATTRIBUTE_CONNECTION)
      .get()
      .createPacket(direction, PacketBuffer(buf))
  }
}
