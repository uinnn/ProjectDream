package dream.packet.game

import dream.entity.base.*
import dream.network.*

/**
 * Serverbound entity attach packet.
 */
data class SPacketEntityAttach(var entityId: Int, var vehicleId: Int, var leashId: Int) : ServerGamePacket {

  constructor(leashId: Int, entity: Entity, vehicle: Entity?) : this(
    entityId = entity.serialId,
    vehicleId = vehicle?.serialId ?: -1,
    leashId = leashId
  )

  constructor(buf: PacketBuffer) : this(buf.readInt(), buf.readInt(), buf.readUnsignedByte().toInt())

  override fun write(buf: PacketBuffer) {
    buf.writeInt(entityId)
    buf.writeInt(vehicleId)
    buf.writeByte(leashId)
  }
}
