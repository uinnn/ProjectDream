package dream.registry

import com.google.common.collect.*
import dream.misc.*

/**
 * Represents a registry based on id's
 */
@Open
public class IdRegistry<T> : Iterable<T> {
   
   /**
    * All registered data.
    */
   val data: BiMap<T, Int> = createUnderlyingMap()
   
   /**
    * The inverse data of this registry.
    */
   val inverseData: BiMap<Int, T> = data.inverse()
   
   /**
    * Gets a registry id from the specified [key].
    */
   operator fun get(key: T) = data[key] ?: -1
   
   /**
    * Gets a registry value from the specified [index].
    */
   operator fun get(index: Int) = inverseData[index]
   
   /**
    * Register a new id [value] with [key] in this registry.
    */
   operator fun set(key: T, value: Int) = data.put(key, value)
   
   override fun iterator() = data.keys.iterator()
   
   fun createUnderlyingMap(): BiMap<T, Int> = HashBiMap.create(512)
}
