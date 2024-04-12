package dream.packet.game

import dream.attribute.AttributeModifier
import dream.attribute.IAttribute
import dream.attribute.Operation
import dream.network.*
import java.util.ArrayList

class SPacketEntityAttributes(
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
        val value = buf.readDouble()
        val operation = Operation.byId(buf.readByte().toInt())
        modifiers += AttributeModifier("Unknown synced attribute modifier", value, operation, id)
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

  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}

/**
 * A snapshot of a attribute used in [SPacketEntityAttributes].
 */
data class AttributeSnapshot(val name: String, val value: Double, val modifiers: Collection<AttributeModifier>)
