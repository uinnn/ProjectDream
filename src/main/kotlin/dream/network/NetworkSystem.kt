package dream.network

import com.google.common.util.concurrent.ThreadFactoryBuilder
import dream.api.Tickable
import dream.chat.text
import dream.errors.Crash
import dream.misc.Open
import dream.packet.handshaking.VanillaHandshakePacketHandler
import dream.packet.login.SPacketDisconnect
import dream.server.Server
import dream.utils.childHandler
import dream.utils.sync
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import java.net.InetAddress

/**
 * Represents an network system.
 *
 * Network system is like a network handler for **server-side** connection.
 */
@Open
class NetworkSystem(val server: Server) : Tickable {

  /**
   * Return if this network system has never had his endpoints terminated.
   */
  @Volatile
  var isAlive = true

  /**
   * All endpoints of this network system.
   */
  val endpoints = ArrayList<ChannelFuture>().sync()

  /**
   * All networks manager of this network system.
   */
  val networks = ArrayList<NetworkManager>().sync()

  /**
   * Adds a channel that listens on publicly accessible network ports
   */
  fun addLanEndpoint(address: InetAddress, port: Int) {
    synchronized(endpoints) {
      val channelClass: Class<out ServerSocketChannel>
      val group: EventLoopGroup

      if (Epoll.isAvailable() && server.shouldUseNativeTransport()) {
        channelClass = EpollServerSocketChannel::class.java
        group = SERVER_EPOLL_EVENTLOOP
      } else {
        channelClass = NioServerSocketChannel::class.java
        group = EVENT_LOOPS
      }

      endpoints += ServerBootstrap()
        .channel(channelClass)
        .childHandler {
          it.config().setOption(ChannelOption.TCP_NODELAY, true)
          it.pipeline()
            .addLast("timeout", ReadTimeoutHandler(30))
            .addLast("legacy_query", PingResponseHandler(this))
            .addLast("splitter", PacketSplitter)
            .addLast("decoder", PacketDecoder(PacketDirection.SERVER))
            .addLast("prepender", PacketPrepender)
            .addLast("encoder", PacketEncoder(PacketDirection.CLIENT))

          val network = NetworkManager(PacketDirection.SERVER)
          network.handler = VanillaHandshakePacketHandler(network, server)
          networks += network
          it.pipeline().addLast("packet_handler", network)
        }
        .group(group)
        .localAddress(address, port)
        .bind()
        .syncUninterruptibly()
    }
  }

  /**
   * Adds a channel that listens locally
   */
  /*fun addLocalEndpoint(): SocketAddress {
    var channel: ChannelFuture

    synchronized(endpoints) {
      channel = ServerBootstrap()
        .channel(LocalServerChannel::class.java)
        .childHandler {
          val network = NetworkManager(PacketDirection.SERVER)
          network.handler = VanillaHandshakePacketHandler(network, server)
          networks += network
          it.pipeline().addLast("packet_handler", network)
        }
        .group(EVENT_LOOPS)
        .localAddress(LocalAddress.ANY)
        .bind()
        .syncUninterruptibly()

      endpoints += channel
    }

    return channel.channel().localAddress()
  }*/

  /**
   * Shuts down all open endpoints.
   */
  fun terminateEndpoints() {
    isAlive = false
    for (point in endpoints)
      point.channel().close().sync()
  }

  /**
   * Will try to process the packets received by each network manager,
   * gracefully manage processing failures and cleans up dead connections.
   */
  override fun tick(partial: Int) {
    networks.forEach { network ->
      if (network.hasChannel) {
        if (!network.isChannelOpen) {
          networks -= network
          network.checkDisconnected()
        } else {
          try {
            network.processPackets(partial)
          } catch (e: Exception) {
            if (network.isLocalChannel) {
              throw Crash("Ticking memory connection", e).category("Ticking Connection", network.toString())
            }

            val text = text("Internal Server Error")
            network.sendPacket(SPacketDisconnect(text)) { network.closeChannel(text) }
            network.disableAutoRead()
          }
        }
      }
    }
  }

  companion object {
    val EVENT_LOOPS by lazy {
      NioEventLoopGroup(
        ThreadFactoryBuilder()
          .setNameFormat("Netty Server IO #%d")
          .setDaemon(true)
          .build()
      )
    }

    val SERVER_EPOLL_EVENTLOOP by lazy {
      EpollEventLoopGroup(
        ThreadFactoryBuilder()
          .setNameFormat("Netty Epoll Server IO #%d")
          .setDaemon(true)
          .build()
      )
    }

    /*val SERVER_LOCAL_EVENTLOOP by lazy {
      LocalEventLoopGroup(
        ThreadFactoryBuilder()
          .setNameFormat("Netty Local Server IO #%d")
          .setDaemon(true)
          .build()
      )
    }*/
  }
}
