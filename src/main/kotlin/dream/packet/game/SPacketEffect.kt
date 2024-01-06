package dream.packet.game

import dream.network.*

class SPacketEffect(
  var entityId: Int,
  var effectId: Byte,
  var amplifier: Byte,
  var duration: Int,
  var hideParticles: Boolean,
) : ServerGamePacket {
  
  val isPermanent get() = duration == 32767
  
  //constructor(entityId: Int, effect: Effect) : this(entityId, effect)
  
  constructor(buf: PacketBuffer) : this(
    entityId = buf.readVarInt(),
    effectId = buf.readByte(),
    amplifier = buf.readByte(),
    duration = buf.readVarInt(),
    hideParticles = buf.readBoolean()
  )
  
  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeByte(effectId.toInt())
    buf.writeByte(amplifier.toInt())
    buf.writeVarInt(duration)
    buf.writeBoolean(hideParticles)
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
  
}
