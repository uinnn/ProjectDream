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
    * Handle client-side [ClientPacketLoginStart] packet.
    */
   fun handleLoginStart(packetIn: ClientPacketLoginStart)
   
   /**
    * Handle client-side [ClientPacketEncryptionResponse] packet.
    */
   fun handleEncryptionResponse(packetIn: ClientPacketEncryptionResponse)
   
   /**
    * Process server-side [ServerPacketEncryptionRequest] packet.
    */
   fun processEncryptionRequest(packet: ServerPacketEncryptionRequest)
   
   /**
    * Process server-side [ServerPacketLoginSuccess] packet.
    */
   fun processLoginSuccess(packet: ServerPacketLoginSuccess)
   
   /**
    * Process server-side [ServerPacketDisconnect] packet.
    */
   fun processDisconnect(packet: ServerPacketDisconnect)
   
   /**
    * Process server-side [ServerPacketEnableCompression] packet.
    */
   fun processEnableCompression(packet: ServerPacketEnableCompression)
}
