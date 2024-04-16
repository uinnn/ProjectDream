package dream.network

import dream.misc.Open
import dream.packet.Packet
import dream.packet.PacketHandler
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToByteEncoder

/**
 * Represents a message encoder for packets.
 */
@Open
class PacketEncoder(val direction: PacketDirection) : MessageToByteEncoder<HandledPacket>() {
  override fun encode(ctx: ChannelHandlerContext, msg: Packet<PacketHandler>, out: ByteBuf) {
    val id = ctx.channel()
      .attr(NetworkManager.ATTRIBUTE_CONNECTION)
      .get()
      .id(direction, msg) ?: error("Can't serialize unregistered packet")

    val buffer = PacketBuffer(out)
    buffer.writeVarInt(id)
    msg.write(buffer)
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

    val buffer = PacketBuffer(buf)
    val id = buffer.readVarInt()

    out += ctx.channel()
      .attr(NetworkManager.ATTRIBUTE_CONNECTION)
      .get()
      .createPacket(direction, id, buffer)
  }
}
