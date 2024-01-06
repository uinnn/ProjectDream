package dream.packet

import dream.entity.player.*
import dream.misc.*
import dream.network.*

/**
 * Represents a base packet.
 *
 * Packets are used to transfer data or information between
 * server and client or vice-versa.
 *
 * To correctly creates an useable packet, any implementation
 * must have a `Constructor` that only have their parameter of [PacketBuffer].
 */
@Open
interface Packet<out T : PacketHandler> {

  /**
   * Writes the raw packet data to the data stream.
   */
  fun write(buf: PacketBuffer)

  /**
   * Passes this Packet on to the [PacketHandler] for processing.
   */
  fun process(handler: @UnsafeVariance T)

  /**
   * Determinates that this packet can be sended to [player].
   */
  fun canSend(player: Player): Boolean {
    return true
  }

  /**
   * Determinates if player can receive this packet.
   */
  fun canReceive(player: Player): Boolean {
    return true
  }
}

/**
 * Represents a packet sended by the client to the server.
 *
 * `Client -> Server`
 */
interface ClientPacket<out T : PacketHandler> : Packet<T>

/**
 * Represents a packet sended by the server to the client.
 *
 * `Server -> Client`
 */
interface ServerPacket<out T : PacketHandler> : Packet<T>

/**
 * Gets the connection state of this packet.
 */
val Packet<*>.state: ConnectionState get() = ConnectionState.byPacket(this)

/**
 * Gets if this packet is a client packet.
 */
inline val Packet<*>.isClient: Boolean get() = this is ClientPacket

/**
 * Gets if this packet is a server packet.
 */
inline val Packet<*>.isServer: Boolean get() = this is ServerPacket
