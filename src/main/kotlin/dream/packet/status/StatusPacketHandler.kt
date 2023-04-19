package dream.packet.status

import dream.packet.*

typealias StatusPacket = Packet<StatusPacketHandler>
typealias ClientStatusPacket = ClientPacket<StatusPacketHandler>
typealias ServerStatusPacket = ServerPacket<StatusPacketHandler>

/**
 * Packet handler for status-related packets.
 *
 * ### Details:
 * * `process` is for server-side packets control.
 * * `handle` is for client-side packets control
 */
interface StatusPacketHandler : PacketHandler {
   
   /**
    * Handle client-side [ClientPacketPing] packet.
    */
   fun handlePing(packet: ClientPacketPing)
   
   /**
    * Handle client-side [ClientPacketServerQuery] packet.
    */
   fun handleServerQuery(packet: ClientPacketServerQuery)
   
   /**
    * Process server-side [ServerPacketPong] packet.
    */
   fun processPong(packet: ServerPacketPong)
   
   /**
    * Process server-side [ServerPacketServerInfo] packet.
    */
   fun processServerInfo(packet: ServerPacketServerInfo)
}
