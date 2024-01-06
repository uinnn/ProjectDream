package dream.packet.handshaking

import dream.network.*

/**
 * Serverbound Handshake packet.
 */
class SPacketHandshake(
  var version: Int,
  var ip: String,
  var port: Int,
  var requestedState: ConnectionState,
) : ServerHandshakePacket {

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

  override fun process(handler: HandshakePacketHandler) {
    handler.processHandshake(this)
  }
}
