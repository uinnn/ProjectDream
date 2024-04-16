package dream.utils

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
 * Shortcut for safely handling bootstrap.
 */
inline fun Bootstrap.handler(crossinline callback: (SocketChannel) -> Unit): Bootstrap {
  return handler(object : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) = callback(ch)
  })
}

/**
 * Shortcut for safely child-handling bootstrap.
 */
inline fun ServerBootstrap.childHandler(crossinline callback: (SocketChannel) -> Unit): ServerBootstrap {
  return childHandler(object : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) = callback(ch)
  })
}
