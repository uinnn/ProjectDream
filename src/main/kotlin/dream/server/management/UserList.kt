package dream.server.management

import dream.misc.*
import kotlinx.serialization.json.*
import java.io.*

@Open
abstract class UserList<K, V : UserEntry<K>>(
  val file: File,
  val map: MutableMap<String, V> = HashMap(),
) : MutableMap<String, V> by map {

  /**
   * Gets if this server is lan-oppened server.
   */
  var isLanServer = true

  /**
   * Creates a entry for this list.
   */
  abstract fun createEntry(data: JsonObject): V

  /**
   * Gets the key used to identify [entry].
   */
  fun getKey(entry: K): String {
    return entry.toString()
  }

  /**
   * Gets if this list has the specified [entry].
   */
  fun has(entry: K): Boolean {
    return containsKey(getKey(entry))
  }

  /**
   * Adds an new entry.
   */
  fun addEntry(entry: V) {
    val value = entry.value ?: return
    put(getKey(value), entry)
  }

  /**
   * Removes an entry.
   */
  fun removeEntry(entry: K): V? {
    return remove(getKey(entry))
  }

  /**
   * Gets an entry value.
   */
  fun getEntry(entry: K): V? {
    return get(getKey(entry))
  }

  /**
   * Removes every entry that's have been expired.
   */
  fun removeExpired() {
    values.removeIf { it.isExpired }
  }

  /**
   * Write the changes madden on [file].
   */
  fun writeChanges() {
    file.outputStream().use {
      Json.encodeToStream(values, it)
    }
  }

  override fun get(key: String): V? {
    removeExpired()
    return map[key]
  }

  override fun put(key: String, value: V): V? {
    val old = map.put(key, value)
    writeChanges()
    return old
  }

  override fun remove(key: String): V? {
    return map.remove(key)?.also { writeChanges() }
  }
}
