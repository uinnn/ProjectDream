package dream.packet.status

import dream.chat.Component
import dream.chat.text
import dream.network.NetworkManager
import dream.server.Server

class VanillaStatusPacketHandler(
  val network: NetworkManager,
  val server: Server = Server.get(),
) : StatusPacketHandler {

  /**
   * Flag to check if the server query has been handled
   */
  var handled = false

  override fun handlePing(packet: CPacketPing) {
    println("received ping packet")
    network.sendPacket(SPacketPong(packet.clientTime))
    network.closeChannel(EXIT_MESSAGE)
  }

  override fun handleServerQuery(packet: CPacketServerQuery) {
    println("received server query packet")
    if (handled) {
      network.closeChannel(EXIT_MESSAGE)
    } else {
      handled = true
      network.sendPacket(SPacketServerInfo(server.statusResponse))
    }
  }

  override fun onDisconnect(reason: Component) {
  }

  companion object {
    val EXIT_MESSAGE = text("Status request has been handled.")
  }
}
