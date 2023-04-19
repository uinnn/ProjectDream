package dream.packet.handshaking

import dream.packet.*

typealias HandshakingPacket = Packet<HandshakingPacketHandler>
typealias ClientHandshakingPacket = ClientPacket<HandshakingPacketHandler>
typealias ServerHandshakingPacket = ServerPacket<HandshakingPacketHandler>

interface HandshakingPacketHandler : PacketHandler {
   fun processHandshake(packet: ServerPacketHandshake)
}
