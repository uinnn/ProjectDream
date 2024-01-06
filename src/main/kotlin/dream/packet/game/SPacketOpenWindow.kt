package dream.packet.game

import dream.chat.*
import dream.network.*

class SPacketOpenWindow(
  var windowId: Int,
  var type: String,
  var title: Component,
  var slots: Int,
  var entityId: Int = 0,
) : ServerGamePacket {
  
  /**
   * Checks if the inventory type is "EntityHorse".
   *
   * This is used to determinate if [entityId] should be writed into [PacketBuffer].
   *
   * @return `true` if the inventory type is "EntityHorse", `false` otherwise.
   */
  val isHorseWindow: Boolean get() = type == "EntityHorse"
  
  /**
   * Checks if the inventory has slots.
   *
   * @return `true` if the inventory has slots, `false` otherwise.
   */
  val hasSlots: Boolean get() = slots > 0
  
  constructor(buf: PacketBuffer) : this(
    windowId = buf.readUnsignedByte().toInt(),
    type = buf.readString(),
    title = buf.readComponent(),
    slots = buf.readUnsignedByte().toInt()
  ) {
    if (isHorseWindow)
      entityId = buf.readInt()
  }
  
  override fun write(buf: PacketBuffer) {
    buf.writeByte(windowId)
    buf.writeString(type)
    buf.writeComponent(title)
    buf.writeByte(slots)
    if (isHorseWindow) {
      buf.writeInt(entityId)
    }
  }
  
  override fun process(handler: GamePacketHandler) {
    TODO("Not yet implemented")
  }
}
