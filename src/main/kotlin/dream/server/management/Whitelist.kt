package dream.server.management

import com.mojang.authlib.*
import kotlinx.serialization.json.*
import java.io.*
import java.util.*

class Whitelist(file: File) : UserList<GameProfile, WhitelistEntry>(file) {
  
  /**
   * If the whitelist is enabled or not.
   */
  var isEnabled = false
  
  /**
   * Gets the names banned.
   */
  val names get() = values.map { it.value!!.name }

  /**
   * Gets the id's banned.
   */
  val ids get() = values.map { it.value!!.id }

  override fun createEntry(data: JsonObject): WhitelistEntry {
    return WhitelistEntry(data)
  }

  override fun getKey(entry: GameProfile): String {
    return entry.id.toString()
  }

  /**
   * Checks if [profile] is whitelisted.
   */
  fun isWhitelisted(profile: GameProfile): Boolean {
    return has(profile)
  }

  /**
   * Checks if a user by [name] is whitelisted.
   */
  fun isWhitelisted(name: String): Boolean {
    return values.any { it.value!!.name.equals(name, true) }
  }

  /**
   * Checks if a user by [id] is whitelisted.
   */
  fun isWhitelisted(id: UUID): Boolean {
    return values.any { it.value!!.id == id }
  }

  /**
   * Find a whitelisted user by [name].
   */
  fun findByName(name: String): WhitelistEntry? {
    return values.find { it.value!!.name.equals(name, true) }
  }
}

