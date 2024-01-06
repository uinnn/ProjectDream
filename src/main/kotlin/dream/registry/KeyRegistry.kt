package dream.registry

import dream.*
import dream.misc.Open

/**
 * Represents a simple key registry
 */
@Open
class KeyRegistry<T : Any> : HashMap<Key, T> {

  val domains: List<String>
    get() = keys.map(Key::domain)

  constructor() : super()
  constructor(capacity: Int) : super(capacity)
  constructor(capacity: Int, factor: Float) : super(capacity, factor)
  constructor(map: Map<Key, T>) : super(map)

  fun getVanilla(key: String): T? {
    return get(minecraftKey(key))
  }

  fun get(domain: String, key: String): T? {
    return get(key(domain, key))
  }

  fun get(parse: String): T? {
    return get(key(parse))
  }

  fun putVanilla(key: String, value: T): T? {
    return put(minecraftKey(key), value)
  }

  fun put(domain: String, key: String, value: T): T? {
    return put(key(domain, key), value)
  }

  fun put(parse: String, value: T): T? {
    return put(key(parse), value)
  }
}
