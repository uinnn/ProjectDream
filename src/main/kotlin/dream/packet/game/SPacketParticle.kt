package dream.packet.game

import dream.network.*
import dream.particle.*

data class SPacketParticle(
  var particle: Particle,
  val longDistance: Boolean,
  var posX: Float,
  val posY: Float,
  val posZ: Float,
  val offsetX: Float,
  val offsetY: Float,
  val offsetZ: Float,
  val speed: Float,
  val amount: Int,
  var arguments: IntArray
) : ServerGamePacket {
  
  constructor(particle: Particle, buf: PacketBuffer) : this(particle,
    longDistance = buf.readBoolean(),
    posX = buf.readFloat(),
    posY = buf.readFloat(),
    posZ = buf.readFloat(),
    offsetX = buf.readFloat(),
    offsetY = buf.readFloat(),
    offsetZ = buf.readFloat(),
    speed = buf.readFloat(),
    amount = buf.readInt(),
    arguments = IntArray(particle.arguments) { buf.readVarInt() }
  )
  
  constructor(buf: PacketBuffer) : this(Particle.byIdOrNull(buf.readInt()) ?: Particle.BARRIER, buf)
  
  override fun write(buf: PacketBuffer) {
    buf.writeInt(particle.id)
    buf.writeBoolean(longDistance)
    buf.writeFloat(posX)
    buf.writeFloat(posY)
    buf.writeFloat(posZ)
    buf.writeFloat(offsetX)
    buf.writeFloat(offsetY)
    buf.writeFloat(offsetZ)
    buf.writeFloat(speed)
    buf.writeInt(amount)
    buf.writeIntArray(arguments) { writeVarInt(it) }
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
