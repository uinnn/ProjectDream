package dream.packet.game

import dream.packet.*

typealias GamePacket = Packet<GamePacketHandler>
typealias ServerGamePacket = ServerPacket<GamePacketHandler>
typealias ClientGamePacket = ClientPacket<GamePacketHandler>

interface GamePacketHandler : PacketHandler
