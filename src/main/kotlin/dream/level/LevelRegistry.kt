@file:Suppress("NOTHING_TO_INLINE")

package dream.level

import dream.collections.*
import dream.errors.*
import java.util.*

/**
 * A manager for levels.
 *
 * This has the responsability to stores and handle every level in a server.
 */
object LevelRegistry {

  val levels = IdMap<Level>()
  
  /**
   * Returns a level by the given [name]
   * or throws [LevelNotFoundException] if is an invalid level.
   */
  operator fun get(name: String) = levels[name] ?: throw LevelNotFoundException("Level $name not found.")

  /**
   * Returns a level by the given [name] or null if is an invalid level.
   */
  fun getOrNull(name: String) = levels[name]

  /**
   * Returns a level by the given [id]
   * or throws [LevelNotFoundException] if is an invalid level.
   */
  operator fun get(id: UUID) = levels[id] ?: throw LevelNotFoundException("Level $id not found.")

  /**
   * Returns a level by the given [id] or null if is an invalid level.
   */
  fun getOrNull(id: UUID) = levels[id]
}

/**
 * Returns a level by the given [name]
 * or throws [LevelNotFoundException] if is an invalid level.
 */
inline fun level(name: String) = LevelRegistry[name]

/**
 * Returns a level by the given [name] or null if is an invalid level.
 */
inline fun levelOrNull(name: String) = LevelRegistry.getOrNull(name)

/**
 * Returns a level by the given [id]
 * or throws [LevelNotFoundException] if is an invalid level.
 */
inline fun level(id: UUID) = LevelRegistry[id]

/**
 * Returns a level by the given [id] or null if is an invalid level.
 */
inline fun levelOrNull(id: UUID) = LevelRegistry.getOrNull(id)
