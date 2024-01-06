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
   * Handle client-side [CPacketPing] packet.
   */
  fun handlePing(packet: CPacketPing)

  /**
   * Handle client-side [CPacketServerQuery] packet.
   */
  fun handleServerQuery(packet: CPacketServerQuery)

  /**
   * Process server-side [SPacketPong] packet.
   */
  fun processPong(packet: SPacketPong)

  /**
   * Process server-side [SPacketServerInfo] packet.
   */
  fun processServerInfo(packet: SPacketServerInfo)
}
