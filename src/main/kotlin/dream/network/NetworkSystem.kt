package dream.network

import com.google.common.util.concurrent.*
import dream.api.*
import dream.chat.*
import dream.errors.*
import dream.misc.*
import dream.packet.handshaking.*
import dream.packet.login.*
import dream.server.*
import dream.utils.*
import io.netty.bootstrap.*
import io.netty.channel.*
import io.netty.channel.epoll.*
import io.netty.channel.nio.*
import io.netty.channel.socket.*
import io.netty.channel.socket.nio.*
import io.netty.handler.timeout.*
import java.net.*

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

  fun start() {
    //val bossGroup = NioEventLoopGroup()
    //val workerGroup = NioEventLoopGroup()
    val b = ServerBootstrap()
    b.group(EVENT_LOOPS)
      .channel(NioServerSocketChannel::class.java)
      //.option(ChannelOption.SO_BACKLOG, 128)
      .childHandler {
        it.config().setOption(ChannelOption.TCP_NODELAY, true)
        it.pipeline()
          .addLast("timeout", ReadTimeoutHandler(30))
          .addLast("legacy_query", PingResponseHandler(this))
          .addLast("splitter", PacketSplitter())
          .addLast("decoder", PacketDecoder(PacketDirection.SERVER))
          .addLast("prepender", PacketPrepender())
          .addLast("encoder", PacketEncoder(PacketDirection.CLIENT))

        val network = NetworkManager(PacketDirection.SERVER)
        networks += network
        it.pipeline().addLast("packet_handler", network)
        network.handler = VanillaHandshakePacketHandler(network, server)
      }


    val f = b.bind(25565).syncUninterruptibly()

    endpoints += f

    f.channel().closeFuture().sync()

  }

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
        .group(group, NioEventLoopGroup())
        .channel(channelClass)
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler {
          it.config().setOption(ChannelOption.TCP_NODELAY, true)
          it.pipeline()
            .addLast("timeout", ReadTimeoutHandler(30))
            .addLast("legacy_query", PingResponseHandler(this))
            .addLast("splitter", PacketSplitter())
            .addLast("decoder", PacketDecoder(PacketDirection.SERVER))
            .addLast("prepender", PacketPrepender())
            .addLast("encoder", PacketEncoder(PacketDirection.CLIENT))

          val network = NetworkManager(PacketDirection.SERVER)
          network.handler = VanillaHandshakePacketHandler(network, server)
          networks += network
          it.pipeline().addLast("packet_handler", network)
        }

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
    synchronized(networks) {
      val iterator = networks.iterator()
      while (iterator.hasNext()) {
        val network = iterator.next()
        if (network.hasChannel) {
          if (!network.isChannelOpen) {
            iterator.remove()
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
