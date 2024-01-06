package dream.packet.handshaking

import dream.packet.*

typealias HandshakingPacket = Packet<HandshakePacketHandler>
typealias ClientHandshakePacket = ClientPacket<HandshakePacketHandler>
typealias ServerHandshakePacket = ServerPacket<HandshakePacketHandler>

/**
 * Base interface for handshake packet handler's.
 */
interface HandshakePacketHandler : PacketHandler {
  fun processHandshake(packet: SPacketHandshake)
}
