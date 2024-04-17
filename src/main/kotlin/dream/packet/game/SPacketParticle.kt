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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SPacketParticle) return false
    if (particle != other.particle) return false
    if (longDistance != other.longDistance) return false
    if (posX != other.posX) return false
    if (posY != other.posY) return false
    if (posZ != other.posZ) return false
    if (offsetX != other.offsetX) return false
    if (offsetY != other.offsetY) return false
    if (offsetZ != other.offsetZ) return false
    if (speed != other.speed) return false
    if (amount != other.amount) return false
    return arguments.contentEquals(other.arguments)
  }

  override fun hashCode(): Int {
    var result = particle.hashCode()
    result = 31 * result + longDistance.hashCode()
    result = 31 * result + posX.hashCode()
    result = 31 * result + posY.hashCode()
    result = 31 * result + posZ.hashCode()
    result = 31 * result + offsetX.hashCode()
    result = 31 * result + offsetY.hashCode()
    result = 31 * result + offsetZ.hashCode()
    result = 31 * result + speed.hashCode()
    result = 31 * result + amount
    result = 31 * result + arguments.contentHashCode()
    return result
  }

}
