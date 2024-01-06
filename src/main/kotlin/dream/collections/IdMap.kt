package dream.collections

import dream.api.*
import java.util.*

/**
 * A map that stores objects of type [V] with associated names and UUIDs.
 * @param V The type of objects stored in the map, must extend [Identifier].
 */
class IdMap<V : Identifier> : Iterable<V> {
  val byName = HashMap<String, V>()
  val byId = HashMap<UUID, V>()
  
  /**
   * Returns the number of elements in the map.
   */
  val size get() = byId.size
  
  /**
   * Returns a set of entries in the map.
   */
  val entries get() = byId.entries
  
  /**
   * Returns a collection of values in the map.
   */
  val values get() = byId.values
  
  /**
   * Returns a set of names in the map.
   */
  val names get() = byName.keys
  
  /**
   * Returns a set of identifiers (UUIDs) in the map.
   */
  val identifiers get() = byId.keys
  
  /**
   * Checks whether the map contains an entry with the specified name.
   * @param name The name to check for.
   * @return `true` if the map contains an entry with the specified name, `false` otherwise.
   */
  operator fun contains(name: String) = name in byName
  
  /**
   * Checks whether the map contains an entry with the specified UUID.
   * @param id The UUID to check for.
   * @return `true` if the map contains an entry with the specified UUID, `false` otherwise.
   */
  operator fun contains(id: UUID) = id in byId
  
  /**
   * Checks whether the map contains the specified value.
   * @param value The value to check for.
   * @return `true` if the map contains the specified value, `false` otherwise.
   */
  operator fun contains(value: V) = value.id in byId
  
  /**
   * Returns the value associated with the specified name.
   * @param name The name of the value to retrieve.
   * @return The value associated with the specified name, or `null` if the name is not found in the map.
   */
  operator fun get(name: String) = byName[name]
  
  /**
   * Returns the value associated with the specified UUID.
   * @param id The UUID of the value to retrieve.
   * @return The value associated with the specified UUID, or `null` if the UUID is not found in the map.
   */
  operator fun get(id: UUID) = byId[id]
  
  /**
   * Puts the specified value into the map with its associated name and UUID.
   * @param value The value to put into the map.
   */
  fun put(value: V) {
    byName[value.name] = value
    byId[value.id] = value
  }
  
  /**
   * Puts the specified value into the map with the specified UUID, name, and value.
   * @param id The UUID associated with the value.
   * @param name The name associated with the value.
   * @param value The value to put into the map.
   */
  fun put(id: UUID, name: String, value: V) {
    byName[name] = value
    byId[id] = value
  }
  
  /**
   * Returns an iterator over the values in the map.
   * @return An iterator over the values in the map.
   */
  override fun iterator(): Iterator<V> = byId.values.iterator()
}
