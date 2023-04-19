package dream.network

import com.google.common.util.concurrent.*
import dream.interfaces.*
import dream.misc.*
import dream.server.*
import dream.utils.*
import io.netty.bootstrap.*
import io.netty.channel.*
import io.netty.channel.epoll.*
import io.netty.channel.local.*
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
   @Volatile var isAlive = true
   
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
                  .addLast("splitter", PacketSplitter())
                  .addLast("decoder", PacketDecoder(PacketDirection.SERVER))
                  .addLast("prepender", PacketPrepender())
                  .addLast("encoder", PacketEncoder(PacketDirection.CLIENT))
               
               val network = NetworkManager(PacketDirection.SERVER)
               // TODO: set network packet handler.
               // network.handler = NetHandlerHandshakeTCP(server, network)
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
   fun addLocalEndpoint(): SocketAddress {
      var channel: ChannelFuture
      
      synchronized(endpoints) {
         channel = ServerBootstrap()
            .channel(LocalServerChannel::class.java)
            .childHandler {
               val network = NetworkManager(PacketDirection.SERVER)
               
               // TODO: set packet handler
               // network.handler = NetHandlerHandshakeMemory(server, network)
               
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
   }
   
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
   override fun tick() {
      synchronized(networks) {
         for (network in networks) {
            if (!network.hasChannel)
               continue
            
            if (!network.isChannelOpen) {
               networks -= network
               network.checkDisconnected()
            } else {
               try {
                  network.processPackets()
               } catch (e: Exception) {
                  // TODO: handle exception
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
      
      val SERVER_LOCAL_EVENTLOOP by lazy {
         LocalEventLoopGroup(
            ThreadFactoryBuilder()
               .setNameFormat("Netty Local Server IO #%d")
               .setDaemon(true)
               .build()
         )
      }
   }
}
