package dream.registry

import com.google.common.collect.*

/**
 * A namespaced registry that's can permit inverse data getters and with id.
 */
class NamespacedRegistry<K, V> : Registry<K, V>() {

  /**
   * All data registered in this registry.
   */
  override val data: BiMap<K, V> = createUnderlyingMap()

  /**
   * The inverse data of this registry.
   */
  val inverseData: BiMap<V, K> = data.inverse()

  /**
   * The id registry data of this registry.
   */
  val identifiers = IdRegistry<V>()

  /**
   * Gets a registry by [id].
   */
  operator fun get(id: Int) = identifiers[id]

  /**
   * Register a new value in this registry.
   */
  fun register(id: Int, key: K, value: V) {
    identifiers[value] = id
    set(key, value)
  }

  /**
   * Gets the id of [value].
   */
  fun getId(value: V) = identifiers[value]

  /**
   * Gets the key of [value].
   */
  fun getKey(value: V) = inverseData[value]

  override fun createUnderlyingMap(): BiMap<K, V> = HashBiMap.create()
}
