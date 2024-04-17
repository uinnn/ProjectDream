package dream.packet.game

import dream.entity.*
import dream.entity.base.*
import dream.network.*

data class SPacketEntityMetadata(
  var entityId: Int,
  var values: List<WatcherValue>
) : ServerGamePacket {
  
  constructor(id: Int, watcher: Watcher, watchedOnly: Boolean = true) :
    this(id, if (watchedOnly) watcher.allWatched else watcher.allChanged)
  
  constructor(entity: Entity, watchedOnly: Boolean = true) :
    this(entity.serialId, entity.watcher, watchedOnly)
  
  constructor(buf: PacketBuffer) : this(buf.readVarInt(), buf.readWatcher())
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeWatcher(values)
  }
}
