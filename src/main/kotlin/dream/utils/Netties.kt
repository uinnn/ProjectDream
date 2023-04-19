package dream.utils

import io.netty.bootstrap.*
import io.netty.channel.*

/**
 * Shortcut for safely handling bootstrap.
 */
inline fun Bootstrap.handler(crossinline callback: (Channel) -> Unit): Bootstrap {
   return handler(object : ChannelInitializer<Channel>() {
      override fun initChannel(ch: Channel) = callback(ch)
   })
}

/**
 * Shortcut for safely child-handling bootstrap.
 */
inline fun ServerBootstrap.childHandler(crossinline callback: (Channel) -> Unit): ServerBootstrap {
   return childHandler(object : ChannelInitializer<Channel>() {
      override fun initChannel(ch: Channel) = callback(ch)
   })
}
