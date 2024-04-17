package dream.packet.game

import com.mojang.authlib.properties.*
import dream.chat.*
import dream.entity.player.*
import dream.network.*
import dream.utils.*
import java.util.*

data class SPacketPlayerListItem(
  var action: PlayerListAction,
  var records: MutableList<PlayerListRecord>
) : ServerGamePacket {

  constructor(buf: PacketBuffer, action: PlayerListAction) : this(
    action,
    buf.readList(ArrayList(), buf.readVarInt()) { PlayerListRecord.readRecord(buf, action) }
  )

  constructor(buf: PacketBuffer) : this(buf, buf.readEnum())

  override fun write(buf: PacketBuffer) {
    buf.writeEnum(action)
    buf.writeVarInt(records.size)
    for (record in records) {
      record.write(buf, action)
    }
  }
}

/**
 * All actions that can be performed on the player list.
 *
 * Used in [SPacketPlayerListItem].
 */
enum class PlayerListAction {
  ADD_PLAYER,
  UPDATE_GAMEMODE,
  UPDATE_LATENCY,
  UPDATE_DISPLAY_NAME,
  REMOVE_PLAYER
}

/**
 * Represents a record for a player in the player list.
 */
data class PlayerListRecord(
  var profile: Profile,
  var ping: Int = 0,
  var gamemode: Gamemode = Gamemode.SURVIVAL,
  var displayName: ComponentText? = null
) {

  /**
   * Gets the profile id for this record.
   */
  val id: UUID get() = profile.id

  /**
   * Gets the profile name for this recod.
   */
  val name: String get() = profile.name

  /**
   * Writes this record to the given buffer.
   */
  fun write(buf: PacketBuffer, action: PlayerListAction) {
    when (action) {
      PlayerListAction.ADD_PLAYER -> {
        buf.writeUUID(id)
        buf.writeString(name)
        buf.writeVarInt(profile.properties.size())
        for (property in profile.properties.values()) {
          buf.writeString(property.name)
          buf.writeString(property.value)
          if (property.hasSignature()) {
            buf.writeBoolean(true)
            buf.writeString(property.signature)
          } else {
            buf.writeBoolean(false)
          }
          buf.writeVarInt(gamemode.id)
          buf.writeVarInt(ping)
          if (displayName == null) {
            buf.writeBoolean(false)
          } else {
            buf.writeBoolean(true)
            buf.writeComponent(displayName!!)
          }
        }
      }

      PlayerListAction.UPDATE_GAMEMODE -> {
        buf.writeUUID(id)
        buf.writeVarInt(gamemode.id)
      }

      PlayerListAction.UPDATE_LATENCY -> {
        buf.writeUUID(id)
        buf.writeVarInt(ping)
      }

      PlayerListAction.UPDATE_DISPLAY_NAME -> {
        buf.writeUUID(id)
        if (displayName == null) {
          buf.writeBoolean(false)
        } else {
          buf.writeBoolean(true)
          buf.writeComponent(displayName!!)
        }
      }

      PlayerListAction.REMOVE_PLAYER -> {
        buf.writeUUID(id)
      }
    }
  }

  companion object {

    /**
     * Reads a record from the given buffer.
     */
    fun readRecord(buf: PacketBuffer, action: PlayerListAction): PlayerListRecord {
      return when (action) {
        PlayerListAction.ADD_PLAYER -> {
          val profile = Profile(buf.readUUID(), buf.readString(16))
          repeat(buf.readVarInt()) {
            val name = buf.readString()
            val value = buf.readString()
            if (buf.readBoolean()) {
              profile.properties.put(name, Property(name, value, buf.readString()))
            } else {
              profile.properties.put(name, Property(name, value))
            }
          }
          val gamemode = Gamemode.byId(buf.readVarInt())
          val ping = buf.readVarInt()
          val displayName = if (buf.readBoolean()) buf.readComponent() else null
          PlayerListRecord(profile, ping, gamemode, displayName)
        }

        PlayerListAction.UPDATE_GAMEMODE -> {
          val profile = Profile(buf.readUUID(), null)
          val gamemode = Gamemode.byId(buf.readVarInt())
          PlayerListRecord(profile, gamemode = gamemode)
        }

        PlayerListAction.UPDATE_LATENCY -> {
          val profile = Profile(buf.readUUID(), null)
          val ping = buf.readVarInt()
          PlayerListRecord(profile, ping = ping)
        }

        PlayerListAction.UPDATE_DISPLAY_NAME -> {
          val profile = Profile(buf.readUUID(), null)
          val displayName = if (buf.readBoolean()) buf.readComponent() else null
          PlayerListRecord(profile, displayName = displayName)
        }

        PlayerListAction.REMOVE_PLAYER -> {
          PlayerListRecord(Profile(buf.readUUID(), null))
        }
      }
    }
  }
}
