package dream.server.management

import com.mojang.authlib.GameProfile
import kotlinx.serialization.json.JsonObject
import java.io.File
import java.util.*

class OperatorsList(file: File) : UserList<GameProfile, OperatorEntry>(file) {

  /**
   * Gets the names banned.
   */
  val names get() = values.map { it.value!!.name }

  /**
   * Gets the id's banned.
   */
  val ids get() = values.map { it.value!!.id }

  override fun createEntry(data: JsonObject): OperatorEntry {
    return OperatorEntry(data)
  }

  override fun getKey(entry: GameProfile): String {
    return entry.id.toString()
  }

  /**
   * Checks if [profile] is operator.
   */
  fun isOp(profile: GameProfile): Boolean {
    return has(profile)
  }

  /**
   * Checks if a user by [name] is operator.
   */
  fun isOp(name: String): Boolean {
    return values.any { it.value!!.name.equals(name, true) }
  }

  /**
   * Checks if a user by [id] is operator.
   */
  fun isOp(id: UUID): Boolean {
    return values.any { it.value!!.id == id }
  }

  /**
   * Find a operator user by [name].
   */
  fun findByName(name: String): OperatorEntry? {
    return values.find { it.value!!.name.equals(name, true) }
  }

  /**
   * Checks if [profile] bypass the player's limits.
   */
  fun bypassLimit(profile: GameProfile): Boolean {
    val entry = getEntry(profile)
    return entry != null && entry.bypassLimit
  }
}
