@file:Suppress("NOTHING_TO_INLINE")

package dream.level

import dream.errors.*
import dream.level.*
import java.util.*

/**
 * A manager for levels.
 *
 * This has the responsability to stores and handle every level in a server.
 */
object LevelRegistry {
   
   /**
    * All levels stored by name.
    */
   val BY_NAME: MutableMap<String, Level> = HashMap()
   
   /**
    * All levels stored by id.
    */
   val BY_ID: MutableMap<UUID, Level> = HashMap()
   
   /**
    * Returns a level by the given [name]
    * or throws [LevelNotFoundException] if is an invalid level.
    */
   operator fun get(name: String) = BY_NAME[name] ?: throw LevelNotFoundException("Level $name not found.")
   
   /**
    * Returns a level by the given [name] or null if is an invalid level.
    */
   fun getOrNull(name: String) = BY_NAME[name]
   
   /**
    * Returns a level by the given [id]
    * or throws [LevelNotFoundException] if is an invalid level.
    */
   operator fun get(id: UUID) = BY_ID[id] ?: throw LevelNotFoundException("Level $id not found.")
   
   /**
    * Returns a level by the given [id] or null if is an invalid level.
    */
   fun getOrNull(id: UUID) = BY_ID[id]
}

/**
 * Returns a level by the given [name]
 * or throws [LevelNotFoundException] if is an invalid level.
 */
inline fun level(name: String) = LevelRegistry[name]

/**
 * Returns a level by the given [name] or null if is an invalid level.
 */
inline fun levelOrNull(name: String) = LevelRegistry.BY_NAME[name]

/**
 * Returns a level by the given [id]
 * or throws [LevelNotFoundException] if is an invalid level.
 */
inline fun level(id: UUID) = LevelRegistry[id]

/**
 * Returns a level by the given [id] or null if is an invalid level.
 */
inline fun levelOrNull(id: UUID) = LevelRegistry.BY_ID[id]
