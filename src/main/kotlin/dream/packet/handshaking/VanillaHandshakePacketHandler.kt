package dream.packet.handshaking

import dream.chat.Component
import dream.chat.text
import dream.network.ConnectionState
import dream.network.NetworkManager
import dream.packet.login.VanillaLoginPacketHandler
import dream.packet.status.VanillaStatusPacketHandler
import dream.server.Server
import korlibs.io.lang.unsupported

/**
 * Vanilla implementation of [HandshakePacketHandler].
 */
class VanillaHandshakePacketHandler(
  val network: NetworkManager,
  val server: Server = Server.get(),
) : HandshakePacketHandler {

  override fun processHandshake(packet: PacketHandshake) {
    println("Receveid handshake packet $packet")
    when (packet.requestedState) {
      ConnectionState.LOGIN -> {
        network.connectionState(ConnectionState.LOGIN)
        val version = packet.version
        when {
          // Client is running in a newer version than 1.8.9
          version > 47 -> network.disconnect(text("Outdated client! Please use 1.8.9"))
          // Client is running in a older version than 1.8.9
          version < 47 -> network.disconnect(text("Outdated server! I'm still on 1.8.9"))
          // Client is running in the same version than server
          else -> network.handler = VanillaLoginPacketHandler(network, server)
        }
      }
      ConnectionState.STATUS -> {
        network.connectionState(ConnectionState.STATUS)
        network.handler = VanillaStatusPacketHandler(network, server)
      }
      else -> unsupported("Invalid intention: $packet.requestedState")
    }
  }

  override fun onDisconnect(reason: Component) {
    println("disconnected beucase ${reason.unformattedText}")
  }
}
