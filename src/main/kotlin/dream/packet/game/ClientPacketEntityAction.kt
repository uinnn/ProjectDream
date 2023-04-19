package dream.packet.game

import dream.entity.*
import dream.network.*

/**
 * Clientbound packet entity action.
 *
 * Called when an entity makes an action.
 */
class ClientPacketEntityAction(
   var id: Int,
   var action: EntityAction,
   var data: Int = 0,
) : ClientGamePacket {
   
   constructor(entity: Entity, action: EntityAction, data: Int = 0) : this(
      entity.serialId,
      action,
      data
   )
   
   constructor(buf: PacketBuffer) : this(
      buf.readVarInt(),
      buf.readEnum(),
      buf.readVarInt()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeVarInt(id)
      buf.writeEnum(action)
      buf.writeVarInt(data)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}

/**
 * All actions that an entity can make.
 *
 * Used on [ClientPacketEntityAction].
 */
enum class EntityAction {
   START_SNEAKING,
   STOP_SNEAKING,
   STOP_SLEEPING,
   START_SPRINTING,
   STOP_SPRINTING,
   RIDING_JUMP,
   OPEN_INVENTORY;
}
