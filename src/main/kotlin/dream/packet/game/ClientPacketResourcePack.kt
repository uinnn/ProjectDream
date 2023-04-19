package dream.packet.game

import dream.network.*

/**
 * Clientbound packet resource pack status.
 *
 * Called when a resource pack is sended to player.
 */
class ClientPacketResourcePack(
   var hash: String,
   var status: ResourcePackStatus,
) : ClientGamePacket {
   
   constructor(buf: PacketBuffer) : this(
      buf.readString(),
      buf.readEnum()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeString(hash)
      buf.writeEnum(status)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}

/**
 * All resource pack status.
 *
 * Used on [ClientPacketResourcePack].
 */
enum class ResourcePackStatus {
   SUCCESSFULLY_LOADED,
   DECLINED,
   FAILED_DOWNLOAD,
   ACCEPTED
}
