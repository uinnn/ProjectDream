package dream.packet

import dream.chat.*
import dream.misc.*
import dream.network.*
import dream.packet.game.*
import dream.packet.handshaking.*
import dream.packet.login.*
import dream.packet.status.*
import io.netty.channel.*

/**
 * Processor handler for packets.
 *
 * ### Details:
 * * `process` is for server-side packets control.
 * * `handle` is for client-side packets control
 */
@Open
interface PacketHandler {
  
  /**
   * Called when any packet is receivied from client.
   */
  fun onReceivePacket(packet: HandledPacket, manager: NetworkManager, context: ChannelHandlerContext) {
    packet.process(this)
  }
  
  /**
   * Simulates a receive packet action from client/server packet types.
   *
   * This can be used to process packet without extra data as on [onReceivePacket].
   *
   * Also this not calls for [Packet.canReceive].
   */
  fun simulateReceivePacket(packet: HandledPacket) {
    packet.process(this)
  }
  
  /**
   * Invoked when disconnecting.
   */
  fun onDisconnect(reason: Component)
}

/**
 * Checks if this handler is a game packet handler.
 */
inline val PacketHandler.isGameHandler: Boolean
  get() = this is GamePacketHandler

/**
 * Checks if this handler is a handshake packet handler.
 */
inline val PacketHandler.isHandshakingHandler: Boolean
  get() = this is HandshakePacketHandler

/**
 * Checks if this handler is a login packet handler.
 */
inline val PacketHandler.isLoginHandler: Boolean
  get() = this is LoginPacketHandler

/**
 * Checks if this handler is a status packet handler.
 */
inline val PacketHandler.isStatusHandler: Boolean
  get() = this is StatusPacketHandler
