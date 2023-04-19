package dream.packet.game

import dream.entity.player.*
import dream.network.*
import dream.utils.*

/**
 * Clientbound packet abilities.
 */
class ClientPacketAbilities(
   var disableDamage: Boolean,
   var isFlying: Boolean,
   var allowFly: Boolean,
   var isCreative: Boolean,
   var flySpeed: Float,
   var walkSpeed: Float,
) : ClientGamePacket {
   
   /**
    * Gets the raw data used to write [disableDamage], [isFlying], [allowFly] and [isCreative].
    */
   val data: Int
      get() = 0
         .setBitsIf(1, disableDamage)
         .setBitsIf(2, isFlying)
         .setBitsIf(4, allowFly)
         .setBitsIf(8, isCreative)
   
   constructor(buf: PacketBuffer) : this(buf.readByte(), buf.readFloat(), buf.readFloat())
   
   constructor(player: Player) : this(
      player.disableDamage,
      player.isFlying,
      player.allowFly,
      player.isCreative,
      player.flySpeed,
      player.walkSpeed
   )
   
   constructor(data: Byte, flySpeed: Float, walkSpeed: Float) : this(
      data.hasBits(1),
      data.hasBits(2),
      data.hasBits(4),
      data.hasBits(8),
      flySpeed,
      walkSpeed
   )
   
   override fun write(buf: PacketBuffer) {
      buf.writeByte(data)
      buf.writeFloat(flySpeed)
      buf.writeFloat(walkSpeed)
   }
   
   override fun process(handler: GamePacketHandler) {
   
   }
}