package dream.collections

/**
 * Mutable map implementation with case-insensitive keys.
 *
 * The [LowerCaseMap] class provides a mutable map implementation where the keys are case-insensitive.
 * The keys are stored in lowercase internally for case-insensitive operations.
 *
 * @param map The underlying mutable map to store the entries. Defaults to an empty HashMap if not provided.
 * @param V The type of values stored in the map.
 */
class LowerCaseMap<V>(private val map: MutableMap<String, V> = HashMap()) : MutableMap<String, V> by map {
  
  /**
   * Retrieves the value associated with the specified key, ignoring case.
   *
   * @param key The key for which to retrieve the value.
   * @return The value associated with the specified key, or `null` if not found.
   */
  override fun get(key: String): V? {
    return map[key.lowercase()]
  }
  
  /**
   * Associates the specified value with the specified key, ignoring case.
   *
   * @param key The key with which to associate the value.
   * @param value The value to be associated with the key.
   * @return The previous value associated with the key, or `null` if not found.
   */
  override fun put(key: String, value: V): V? {
    return map.put(key.lowercase(), value)
  }
  
  /**
   * Removes the mapping for the specified key, ignoring case.
   *
   * @param key The key for which to remove the mapping.
   * @return The previous value associated with the key, or `null` if not found.
   */
  override fun remove(key: String): V? {
    return map.remove(key.lowercase())
  }
  
  /**
   * Checks if the map contains the specified key, ignoring case.
   *
   * @param key The key to check.
   * @return `true` if the map contains the key, `false` otherwise.
   */
  override fun containsKey(key: String): Boolean {
    return map.containsKey(key.lowercase())
  }
}
