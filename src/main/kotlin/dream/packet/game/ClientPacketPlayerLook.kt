package dream.packet.game

import dream.entity.*
import dream.network.*

/**
 * Clientbound packet player look.
 */
class ClientPacketPlayerLook(
   yaw: Float,
   pitch: Float,
   ground: Boolean,
) : ClientPacketPlayerInfo(yaw = yaw, pitch = pitch, onGround = ground, isRotating = true) {
   
   constructor(entity: Entity) : this(entity.yaw, entity.pitch, entity.onGround)
   
   constructor(buf: PacketBuffer) : this(
      buf.readFloat(),
      buf.readFloat(),
      buf.readBoolean()
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeFloat(yaw)
      buf.writeFloat(pitch)
      super.write(buf)
   }
}
