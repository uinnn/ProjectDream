package dream.packet.login

import dream.packet.*

typealias LoginPacket = Packet<LoginPacketHandler>
typealias ClientLoginPacket = ClientPacket<LoginPacketHandler>
typealias ServerLoginPacket = ServerPacket<LoginPacketHandler>

/**
 * Packet handler for login-related packets.
 *
 * ### Details:
 * * `process` is for server-side packets control.
 * * `handle` is for client-side packets control
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

  /**
   * Process server-side [SPacketEncryptionRequest] packet.
   */
  fun processEncryptionRequest(packet: SPacketEncryptionRequest)

  /**
   * Process server-side [SPacketLoginSuccess] packet.
   */
  fun processLoginSuccess(packet: SPacketLoginSuccess)

  /**
   * Process server-side [SPacketDisconnect] packet.
   */
  fun processDisconnect(packet: SPacketDisconnect)

  /**
   * Process server-side [SPacketEnableCompression] packet.
   */
  fun processEnableCompression(packet: SPacketEnableCompression)
}
