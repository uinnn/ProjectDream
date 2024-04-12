package dream.network

import com.google.common.collect.*
import com.google.common.util.concurrent.*
import dream.api.*
import dream.chat.*
import dream.entity.player.*
import dream.misc.*
import dream.packet.*
import dream.packet.login.SPacketDisconnect
import dream.utils.*
import io.netty.bootstrap.*
import io.netty.channel.*
import io.netty.channel.epoll.*
import io.netty.channel.local.*
import io.netty.channel.nio.*
import io.netty.channel.socket.nio.*
import io.netty.handler.timeout.*
import io.netty.util.*
import io.netty.util.concurrent.*
import io.netty.util.concurrent.Future
import java.net.*
import java.util.concurrent.*
import java.util.concurrent.locks.*
import javax.crypto.*

typealias HandledPacket = Packet<PacketHandler>
typealias PacketChannelHandler = SimpleChannelInboundHandler<HandledPacket>
typealias PacketListener = GenericFutureListener<Future<Void>>
typealias PacketHandlerPair = Pair<HandledPacket, Array<out PacketListener>?>

/**
 * A manager for network.
 *
 * This has the responsability to read packets from a channel.
 *
 * Network manager is a network handler for **client-side** connections.
 */
@Open
class NetworkManager(val direction: PacketDirection) : PacketChannelHandler() {

  /**
   * The active channel of this network manager.
   */
  lateinit var channel: Channel

  /**
   * The remote party address of this network manager.
   */
  lateinit var address: SocketAddress

  /**
   * The packet handler to process all packets being sended in this channel.
   */
  lateinit var handler: PacketHandler

  /**
   * Gets the packet handler of this network as [PlayerConnection].
   *
   * Can be null if [handler] is not a [PlayerConnection].
   *
   * Will be null only if a custom implementation of packet handler is aside.
   */
  val connection get() = handler as? PlayerConnection

  /**
   * Gets the player that own this network.
   *
   * Can be null if [handler] is not a [PlayerConnection].
   *
   * Will be null only if a custom implementation of packet handler is aside.
   */
  val player get() = connection?.player

  /**
   * All packets to be outbounded.
   */
  val outboundPackets: ConcurrentLinkedQueue<PacketHandlerPair> = Queues.newConcurrentLinkedQueue()

  /**
   * Locker of this network.
   */
  private val lock = ReentrantReadWriteLock()

  /**
   * The reason that's this network has been shutdown.
   */
  var terminationReason: Component? = null

  /**
   * Returns if this network is encrypted.
   */
  var isEncrypted = false

  /**
   * Returns if this network has been disconnected.
   */
  var isDisconnected = false

  /**
   * Returns if this network has an active channel setted.
   */
  val hasChannel: Boolean
    get() = this::channel.isInitialized

  /**
   * Returns if this network has an active channel.
   */
  val isChannelOpen: Boolean
    get() = hasChannel && channel.isOpen

  /**
   * Returns if the active channel of this network is local.
   */
  val isLocalChannel: Boolean
    get() = channel is LocalChannel || channel is LocalServerChannel

  override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet<PacketHandler>) {
    if (channel.isOpen) {
      try {
        handler.onReceivePacket(msg, this, ctx)
      } catch (e: Exception) {
        println("An error happened while trying to process packet \n $msg for $this")
        exceptionCaught(ctx, e)
      }
    }
  }

  override fun channelActive(context: ChannelHandlerContext) {
    super.channelActive(context)
    channel = context.channel()
    address = channel.remoteAddress()
    connectionState(ConnectionState.HANDSHAKING)
  }

  override fun channelInactive(context: ChannelHandlerContext) {
    closeChannel(text("Disconnected. Check internet connection.")) // TODO: translated reason
  }

  @Deprecated("Deprecated in Java")
  override fun exceptionCaught(context: ChannelHandlerContext, cause: Throwable) {
    closeChannel(
      text(
        """
         Please contact a Admin.
         An error occured: $cause
       """.trimIndent()
      )
    ) // TODO: translated reason
  }

  /**
   * Sends the given [packet] to this network and returns itself.
   */
  fun sendPacket(packet: HandledPacket): NetworkManager {
    if (channel.isOpen) {
      flushOutboundQueue()
      dispatchPacket(packet, null)
    } else {
      lock.writeLock().lock()
      outboundPackets += packet to null
      lock.writeLock().unlock()
    }

    return this
  }

  /**
   * Sends the given [packet] to this network and returns itself.
   */
  fun sendPacket(packet: HandledPacket, vararg listeners: PacketListener): NetworkManager {
    if (channel.isOpen) {
      flushOutboundQueue()
      dispatchPacket(packet, listeners)
    } else {
      lock.writeLock().lock()
      outboundPackets += packet to listeners
      lock.writeLock().unlock()
    }

    return this
  }

  /**
   * Sends the given [packet] to this network and returns itself.
   */
  fun sendPacket(packet: HandledPacket, listener: PacketListener): NetworkManager {
    return sendPacket(packet, listeners = arrayOf(listener))
  }

  /**
   * Shortcut for dispatching the same code twice.
   */
  protected fun dispatchPacket(
    packet: HandledPacket,
    listener: Array<out PacketListener>?,
    packetState: ConnectionState,
    channelState: ConnectionState,
  ) {
    if (packetState != channelState) {
      connectionState(packetState)
    }

    val channel = channel.writeAndFlush(packet)
    if (listener != null) {
      channel.addListeners(*listener)
    }

    channel.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  /**
   * Will commit the packet to the channel.
   *
   * If the current thread 'owns' the channel it will write and flush the
   * packet, otherwise it will add a task for the channel eventloop thread to do that.
   */
  fun dispatchPacket(packet: HandledPacket, listener: Array<out PacketListener>? = null) {
    val packetState = packet.state
    val channelState = channel.attr(ATTRIBUTE_CONNECTION).get()

    if (packetState != channelState) {
      channel.config().isAutoRead = false
    }

    val loop = channel.eventLoop()
    if (loop.inEventLoop()) {
      dispatchPacket(packet, listener, packetState, channelState)
    } else {
      loop.execute {
        dispatchPacket(packet, listener, packetState, channelState)
      }
    }
  }

  /**
   * Will commit the packet to the channel.
   *
   * If the current thread 'owns' the channel it will write and flush the
   * packet, otherwise it will add a task for the channel eventloop thread to do that.
   */
  fun dispatchPacket(pair: PacketHandlerPair) = dispatchPacket(pair.first, pair.second)

  /**
   * Changes the connection state of the current active channel.
   */
  fun connectionState(state: ConnectionState) {
    channel.attr(ATTRIBUTE_CONNECTION).set(state)
    channel.config().isAutoRead = true
  }

  /**
   * Will iterate through the [outboundPackets] and dispatch all Packets
   */
  fun flushOutboundQueue() {
    if (isChannelOpen) {
      lock.readLock().lock()
      outboundPackets.forEachPoll(::dispatchPacket)
      lock.readLock().unlock()
    }
  }

  /**
   * Checks timeouts and processes all packets received
   */
  fun processPackets(partial: Int = -1) {
    flushOutboundQueue()

    if (handler is Tickable) {
      (handler as Tickable).tick(partial)
    }

    channel.flush()
  }

  /**
   * Closes the channel with [reason] as exit message.
   */
  fun closeChannel(reason: Component) {
    if (channel.isOpen) {
      channel.close().awaitUninterruptibly()
      terminationReason = reason
    }
  }

  /**
   * Disconnects this network manager sending them a disconnect packet and closing the channel.
   */
  fun disconnect(reason: Component) {
    sendPacket(SPacketDisconnect(reason))
    closeChannel(reason)
  }

  /**
   * Adds an encryptor + decryptor to the channel pipeline.
   *
   * @param key the secret key used for encrypted communication
   */
  fun enableEncryption(key: SecretKey) {
    isEncrypted = true
    channel.pipeline().apply {
      addBefore("splitter", "decrypt", PacketDecryptor(key.createNetCipher(2)))
      addBefore("prepender", "encrypt", PacketDecryptor(key.createNetCipher(1)))
    }
  }

  /**
   * Changes the compression threshold of this network by [threshold].
   */
  fun compressionThreshold(threshold: Int) {
    val pipe = channel.pipeline()
    val decompressor = pipe["decompress"]
    val compressor = pipe["compress"]

    if (threshold >= 0) {
      if (decompressor is PacketDecompressor) {
        decompressor.threshold = threshold
      } else {
        pipe.addBefore("decoder", "decompress", PacketDecompressor(threshold))
      }

      if (compressor is PacketCompressor) {
        compressor.threshold = threshold
      } else {
        pipe.addBefore("encoder", "compress", PacketCompressor(threshold))
      }

    } else {
      if (decompressor is PacketDecompressor) pipe.remove("decompress")
      if (compressor is PacketCompressor) pipe.remove("compress")
    }
  }

  /**
   * Switches the channel to manual reading modus
   */
  fun disableAutoRead() {
    channel.config().isAutoRead = false
  }

  fun checkDisconnected() {
    if (isDisconnected || !hasChannel || channel.isOpen)
      return

    isDisconnected = true

    when {
      terminationReason != null -> handler.onDisconnect(terminationReason!!)
      this::handler.isInitialized -> handler.onDisconnect(text("Disconnected."))
    }
  }

  companion object {

    @JvmField
    val ATTRIBUTE_CONNECTION: AttributeKey<ConnectionState> = AttributeKey.valueOf("protocol")

    val CLIENT_NIO_EVENTLOOP by lazy {
      NioEventLoopGroup(
        ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build()
      )
    }

    val CLIENT_EPOLL_EVENTLOOP by lazy {
      EpollEventLoopGroup(
        ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build()
      )
    }

    /*val CLIENT_LOCAL_EVENTLOOP by lazy {
      LocalEventLoopGroup(
        ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build()
      )
    }*/

    /**
     * Create a new network manager from the server host and connect it to the server
     *
     * @param address The address of the server
     * @param port The server port
     * @param nativeTransport True if the client use the native transport system
     */
    fun createAndConnect(address: InetAddress, port: Int, nativeTransport: Boolean): NetworkManager {
      val network = NetworkManager(PacketDirection.CLIENT)
      val useNative = Epoll.isAvailable() && nativeTransport

      Bootstrap()
        .group(if (useNative) CLIENT_EPOLL_EVENTLOOP else CLIENT_NIO_EVENTLOOP)
        .handler {
          it.config().setOption(ChannelOption.TCP_NODELAY, true)
          it.pipeline()
            .addLast("timeout", ReadTimeoutHandler(30))
            .addLast("splitter", PacketSplitter)
            .addLast("decoder", PacketDecoder(PacketDirection.CLIENT))
            .addLast("prepender", PacketPrepender)
            .addLast("encoder", PacketEncoder(PacketDirection.SERVER))
            .addLast("packet_handler", network)
        }
        .channel(if (useNative) EpollSocketChannel::class.java else NioSocketChannel::class.java)
        .connect(address, port)
        .syncUninterruptibly()

      return network
    }

    /**
     * Prepares a client-side network manager.
     *
     * Establishes a connection to the socket supplied
     * and configures the channel pipeline.
     */
    /*fun provideLocalClient(address: SocketAddress): NetworkManager {
      val network = NetworkManager(PacketDirection.CLIENT)

      Bootstrap()
        .group(CLIENT_LOCAL_EVENTLOOP)
        .handler { it.pipeline().addLast("packet_handler", network) }
        .channel(LocalChannel::class.java)
        .connect(address)
        .syncUninterruptibly()

      return network
    }*/
  }
}
