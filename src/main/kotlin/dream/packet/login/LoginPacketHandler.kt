package dream.packet.login

import dream.packet.*

typealias LoginPacket = Packet<LoginPacketHandler>
typealias ClientLoginPacket = ClientPacket<LoginPacketHandler>
typealias ServerLoginPacket = ServerPacket<LoginPacketHandler>

/**
 * Packet handler for login-related packets.
 */
interface LoginPacketHandler : PacketHandler {

  /**
   * Handle client-side [CPacketLoginStart] packet.
   */
  fun handleLoginStart(packetIn: CPacketLoginStart)

  /**
   * Handle client-side [CPacketEncryptionResponse] packet.
   */
  fun handleEncryptionResponse(packetIn: CPacketEncryptionResponse)

}
