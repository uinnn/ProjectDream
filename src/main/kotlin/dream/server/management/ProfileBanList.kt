package dream.server.management

import com.mojang.authlib.GameProfile
import kotlinx.serialization.json.JsonObject
import java.io.File
import java.util.*

class ProfileBanList(file: File) : UserList<GameProfile, ProfileBanEntry>(file) {

  /**
   * Gets the names banned.
   */
  val names get() = values.map { it.value!!.name }

  /**
   * Gets the id's banned.
   */
  val ids get() = values.map { it.value!!.id }

  override fun createEntry(data: JsonObject): ProfileBanEntry {
    return ProfileBanEntry(data)
  }

  override fun getKey(entry: GameProfile): String {
    return entry.id.toString()
  }

  /**
   * Checks if [profile] is banned.
   */
  fun isBanned(profile: GameProfile): Boolean {
    return has(profile)
  }

  /**
   * Checks if a user by [name] is banned.
   */
  fun isBanned(name: String): Boolean {
    return values.any { it.value!!.name.equals(name, true) }
  }

  /**
   * Checks if a user by [id] is banned.
   */
  fun isBanned(id: UUID): Boolean {
    return values.any { it.value!!.id == id }
  }

  /**
   * Find a banned user by [name].
   */
  fun findByName(name: String): ProfileBanEntry? {
    return values.find { it.value!!.name.equals(name, true) }
  }
}
