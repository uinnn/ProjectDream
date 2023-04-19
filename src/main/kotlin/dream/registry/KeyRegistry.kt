package dream.registry

import dream.*
import dream.misc.*

/**
 * Represents a simple key registry
 */
@Open
public class KeyRegistry<T : Any> : HashMap<Key, T> {
   
   public val domains: List<String>
      get() = keys.map(Key::domain)
   
   constructor() : super()
   constructor(capacity: Int) : super(capacity)
   constructor(capacity: Int, factor: Float) : super(capacity, factor)
   constructor(map: Map<Key, T>) : super(map)
   
   public fun getVanilla(key: String): T? {
      return get(minecraftKey(key))
   }
   
   public fun get(domain: String, key: String): T? {
      return get(key(domain, key))
   }
   
   public fun get(parse: String): T? {
      return get(key(parse))
   }
   
   public fun putVanilla(key: String, value: T): T? {
      return put(minecraftKey(key), value)
   }
   
   public fun put(domain: String, key: String, value: T): T? {
      return put(key(domain, key), value)
   }
   
   public fun put(parse: String, value: T): T? {
      return put(key(parse), value)
   }
}
