package dream.packet

import dream.chat.*
import dream.packet.game.*
import dream.packet.handshaking.*
import dream.packet.login.*
import dream.packet.status.*

/**
 * Processor handler for packets.
 *
 * ### Details:
 * * `process` is for server-side packets control.
 * * `handle` is for client-side packets control
 */
interface PacketHandler {
   
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
   get() = this is HandshakingPacketHandler

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
