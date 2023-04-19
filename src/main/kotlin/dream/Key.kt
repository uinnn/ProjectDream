@file:Suppress("NOTHING_TO_INLINE")

package dream

import dream.misc.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.*

/**
 * Represents a namespaced key.
 *
 * Key is a unique identifier to handle things in minecraft.
 */
@Serializable(KeySerializer::class)
@Open
public data class Key internal constructor(val domain: String, val key: String) {
   public override fun toString() = "$domain:$key"
   
   companion object Factory {
      
      /**
       * Creates a new namespaced key by the given values.
       */
      @JvmStatic
      public fun of(domain: String, key: String) = Key(domain.lowercase(), key.lowercase())
      
      /**
       * Parses a namespaced key by the given [str].
       *
       * ### Example of valid values:
       * ```
       * "minecraft:cow"
       * "dream:supercow"
       * ```
       */
      @JvmStatic
      public fun parse(str: String): Key {
         val split = str.split(':', limit = 2)
         return of(split[0], split[1])
      }
      
      /**
       * Creates a new namespaced key from the minecraft.
       */
      @JvmStatic
      public fun minecraft(key: String) = Key("minecraft", key.lowercase())
   }
}

/**
 * Empty Vanilla Key.
 */
public object EmptyKey : Key("minecraft", "empty")

/**
 * Creates a new namespaced key by the given values.
 */
public inline fun key(domain: String, key: String) = Key.of(domain, key)

/**
 * Parses a namespaced key by the given [str].
 *
 * ### Example of valid values:
 * ```
 * "minecraft:cow"
 * "dream:supercow"
 * ```
 */
public inline fun key(str: String) = Key.parse(str)

/**
 * Creates a new namespaced key from the minecraft.
 */
public inline fun minecraftKey(key: String) = Key.minecraft(key)
