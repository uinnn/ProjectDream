package dream.packet.game

import dream.attribute.*
import dream.network.*

data class SPacketEntityAttributes(
  var entityId: Int,
  var snapshots: MutableList<AttributeSnapshot> = ArrayList(4)
) : ServerGamePacket {

  constructor(entityId: Int, attributes: Collection<IAttribute>) : this(entityId) {
    for (attribute in attributes) {
      snapshots += AttributeSnapshot(attribute.type.unlocalName, attribute.value, attribute.allModifiers)
    }
  }

  constructor(buf: PacketBuffer) : this(buf.readVarInt()) {
    repeat(buf.readInt()) {
      val name = buf.readString()
      val value = buf.readDouble()
      val modifiers = ArrayList<AttributeModifier>()
      repeat(buf.readVarInt()) {
        val id = buf.readUUID()
        val amount = buf.readDouble()
        val operation = Operation.byId(buf.readByte().toInt())
        modifiers += AttributeModifier("Unknown synced attribute modifier", amount, operation, id)
      }
      snapshots += AttributeSnapshot(name, value, modifiers)
    }
  }

  override fun write(buf: PacketBuffer) {
    buf.writeVarInt(entityId)
    buf.writeInt(snapshots.size)
    for (snapshot in snapshots) {
      buf.writeString(snapshot.name)
      buf.writeDouble(snapshot.value)
      buf.writeVarInt(snapshot.modifiers.size)
      for (modifier in snapshot.modifiers) {
        buf.writeUUID(modifier.id)
        buf.writeDouble(modifier.amount)
        buf.writeByte(modifier.operation.id)
      }
    }
  }
}

/**
 * A snapshot of a attribute used in [SPacketEntityAttributes].
 */
data class AttributeSnapshot(val name: String, val value: Double, val modifiers: Collection<AttributeModifier>)
