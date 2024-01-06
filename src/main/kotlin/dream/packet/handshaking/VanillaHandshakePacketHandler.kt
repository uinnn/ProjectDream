package dream.packet.handshaking

import dream.chat.*
import dream.network.*
import dream.server.*
import io.netty.channel.*

/**
 * Vanilla implementation of [HandshakePacketHandler].
 */
class VanillaHandshakePacketHandler(
  val network: NetworkManager,
  val server: Server = Server.get(),
) : HandshakePacketHandler {

  override fun processHandshake(packet: SPacketHandshake) {
    network.connectionState(packet.requestedState)
    network.handler
  }
  
  override fun onReceivePacket(packet: HandledPacket, manager: NetworkManager, context: ChannelHandlerContext) {
    TODO("Not yet implemented")
  }
  
  override fun simulateReceivePacket(packet: HandledPacket) {
    TODO("Not yet implemented")
  }
  
  override fun onDisconnect(reason: Component) {
  }
}
