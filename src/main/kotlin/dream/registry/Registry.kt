package dream.registry

import dream.misc.*
import dream.utils.*

/**
 * Represents a registry to handles data.
 */
@Open
class Registry<K, V> : Iterable<MutableEntry<K, V>> {

  /**
   * All data registered in this registry.
   */
  val data: MutableMap<K, V> = createUnderlyingMap()

  /**
   * Returns the keys of this registry.
   */
  val keys get() = data.keys

  /**
   * Returns the values of this registry.
   */
  val values get() = data.values

  /**
   * Gets a registered data in this registry.
   */
  operator fun get(key: K) = data[key]

  /**
   * Registers a new data value in this registry.
   */
  operator fun set(key: K, value: V) = data.put(key, value)

  /**
   * Returns if this registry contains [key].
   */
  operator fun contains(key: K) = key in data

  /**
   * Create an underlying map to declare new raw [data].
   */
  fun createUnderlyingMap(): MutableMap<K, V> = HashMap()

  override fun iterator() = data.iterator()
}
