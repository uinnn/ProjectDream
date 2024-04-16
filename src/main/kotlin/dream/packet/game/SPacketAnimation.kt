package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Serverbound Animation packet.
 */
data class SPacketAnimation(var entityId: Int, var animation: EntityAnimation) : ServerGamePacket {

  constructor(entity: Entity, type: EntityAnimation) : this(entity.serialId, type)
  
  constructor(entityId: Int, animationId: Int) : this(entityId, EntityAnimation.byId(animationId))
  
  constructor(buf: PacketBuffer) : this(
    buf.readVarInt(),
    buf.readUnsignedByte().toInt()
  )

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(animation.id)
  }

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

/**
 * All animations of an entity.
 *
 * Used on [SPacketAnimation].
 */
enum class EntityAnimation(val id: Int) {
  SWING(0),
  HURT(1),
  WAKE_UP(2),
  CRIT_HIT(4),
  MAGICAL_CRIT_HIT(5);

  companion object {

    /**
     * Gets an entity animation by id.
     */
    fun byId(id: Int): EntityAnimation {
      return when (id) {
        1 -> HURT
        2 -> WAKE_UP
        4 -> CRIT_HIT
        5 -> MAGICAL_CRIT_HIT
        else -> SWING
      }
    }
  }
}
