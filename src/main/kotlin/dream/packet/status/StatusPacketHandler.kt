package dream.packet.status

import dream.packet.*

typealias StatusPacket = Packet<StatusPacketHandler>
typealias ClientStatusPacket = ClientPacket<StatusPacketHandler>
typealias ServerStatusPacket = ServerPacket<StatusPacketHandler>

/**
 * Packet handler for status-related packets.
 */
interface StatusPacketHandler : PacketHandler {

  /**
   * Handle client-side [CPacketPing] packet.
   */
  fun handlePing(packet: CPacketPing)

  /**
   * Handle client-side [CPacketServerQuery] packet.
   */
  fun handleServerQuery(packet: CPacketServerQuery)
}
