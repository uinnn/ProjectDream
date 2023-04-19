package dream.network

import io.netty.buffer.*
import io.netty.channel.*

/**
 * Represents a handler for ping response.
 */
open class PingResponseHandler(val network: NetworkSystem) : ChannelInboundHandlerAdapter() {
   
   override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
      val buffer = msg as ByteBuf
      buffer.markReaderIndex()
      if (buffer.readUnsignedByte() != 254.toShort())
         return
      
      var reset = true
      
      try {
         val server = network.server
         
         /**
          * TODO:
          * Encodes MOTD, Version, Player count/max in buffer.
          */
         when (buffer.readableBytes()) {
            // minecraft version 1.3 or below
            0 -> writeAndFlush(ctx, getStringBuffer(""))
            
            // minecraft version 1.4-1.5
            1 -> {
               if (buffer.readUnsignedByte() != 1.toShort())
                  return
               
               writeAndFlush(ctx, getStringBuffer(""))
            }
            
            // minecraft version 1.6 or higher
            else -> {
               if (!buffer.result())
                  return
               
               val stringBuffer = getStringBuffer("")
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
         
      } catch (ex: Exception) {
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
      var base = readUnsignedByte() == 1.toShort()
      base = base and (readUnsignedByte() == 250.toShort())
      base = base and (String(readBytes(readShort() * 2).array(), Charsets.UTF_16BE) == "MC|PingHost")
      val max = readUnsignedShort()
      base = base and (readUnsignedByte() >= 73)
      base = base and (7 + readBytes(readShort() * 2).array().size == max)
      base = base and (readInt() <= 65535)
      base = base and (readableBytes() == 0)
      return base
   }
}
