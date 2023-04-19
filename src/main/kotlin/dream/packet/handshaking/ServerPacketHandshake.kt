package dream.packet.handshaking

import dream.network.*

/**
 * Serverbound Handshake packet.
 */
class ServerPacketHandshake(
   var version: Int,
   var ip: String,
   var port: Int,
   var requestedState: ConnectionState,
) : ServerHandshakingPacket {
   
   constructor(buf: PacketBuffer) : this(
      buf.readVarInt(),
      buf.readString(),
      buf.readUnsignedShort(),
      ConnectionState.byId(buf.readVarInt())
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(version)
      buf.writeString(ip)
      buf.writeShort(port)
      buf.writeVarInt(requestedState.id)
   }
   
   override fun process(handler: HandshakingPacketHandler) {
      handler.processHandshake(this)
   }
}
