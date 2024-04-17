package dream.packet.game

import dream.damage.*
import dream.network.*

data class SPacketCombat(
  var event: CombatEvent,
  var playerId: Int = -1,
  var entityId: Int = -1,
  var duration: Int = -1,
  var deathMessage: String = ""
) : ServerGamePacket {

  constructor(tracker: CombatTrack, event: CombatEvent) : this(event) {
    if (event == CombatEvent.ENTITY_DIED) {
      val killer = tracker.searchKiller()
      playerId = tracker.entity.serialId
      entityId = killer?.serialId ?: -1
      deathMessage = tracker.getDeathMessage().unformattedText
    } else if (event == CombatEvent.END_COMBAT) {
      val killer = tracker.searchKiller()
      duration = tracker.combatDuration
      entityId = killer?.serialId ?: -1
    }
  }

  constructor(buf: PacketBuffer, event: CombatEvent) : this(event) {
    if (event == CombatEvent.ENTITY_DIED) {
      playerId = buf.readVarInt()
      entityId = buf.readInt()
      deathMessage = buf.readString()
    } else if (event == CombatEvent.END_COMBAT) {
      duration = buf.readVarInt()
      entityId = buf.readInt()
    }
  }

  constructor(buf: PacketBuffer) : this(buf, buf.readEnum())

  override fun write(buf: PacketBuffer) {
    buf.writeEnum(event)
    if (event == CombatEvent.ENTITY_DIED) {
      buf.writeVarInt(playerId)
      buf.writeInt(entityId)
      buf.writeString(deathMessage)
    } else if (event == CombatEvent.END_COMBAT) {
      buf.writeVarInt(duration)
      buf.writeInt(entityId)
    }
  }
}

/**
 * Represents all combat events that can happen.
 *
 * Used in [SPacketCombat].
 */
enum class CombatEvent {
  ENTER_COMBAT,
  END_COMBAT,
  ENTITY_DIED
}
