package dream.packet.game

import dream.entity.*
import dream.entity.player.*
import dream.network.*
import java.util.*

/**
 * Serverbound packet spawn player.
 */
class ServerPacketSpawnPlayer(
   var id: Int,
   var uuid: UUID,
   var x: Int,
   var y: Int,
   var z: Int,
   var yaw: Byte,
   var pitch: Byte,
   var currentItem: Int,
   var watcher: Watcher? = null,
   protected var values: List<WatcherValue>? = null,
) : ServerGamePacket {
   
   /**
    * Gets the watcher values of this packet.
    */
   val watcherValues: List<WatcherValue>
      get() {
         if (values == null) {
            values = watcher?.allWatched
         }
         
         return values ?: emptyList()
      }
   
   constructor(player: Player) : this(
      player.serialId,
      player.id,
      posToPacket(player.x),
      posToPacket(player.y),
      posToPacket(player.z),
      rotationToPacketByte(player.yaw),
      rotationToPacketByte(player.pitch),
      player.heldItem.id,
      player.watcher,
   )
   
   constructor(buf: PacketBuffer) : this(
      buf.readVarInt(),
      buf.readUUID(),
      buf.readInt(),
      buf.readInt(),
      buf.readInt(),
      buf.readByte(),
      buf.readByte(),
      buf.readShort().toInt(),
      null,
      Watcher.readObjects(buf)
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(id)
      buf.writeUUID(uuid)
      buf.writeInt(x)
      buf.writeInt(y)
      buf.writeInt(z)
      buf.writeByte(yaw.toInt())
      buf.writeByte(pitch.toInt())
      buf.writeShort(currentItem)
      watcher?.write(buf)
   }
   
   override fun process(handler: GamePacketHandler) {
      TODO("Not yet implemented")
   }
}
