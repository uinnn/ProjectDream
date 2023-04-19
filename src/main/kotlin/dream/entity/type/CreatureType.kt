package dream.entity.type

import dream.*
import dream.entity.*
import kotlin.reflect.*

/**
 * Represents a entity type for creatures.
 */
open class CreatureType<T : Creature>(
   type: KClass<T>,
   id: Int,
   key: Key,
   name: String,
   isAlive: Boolean = false,
) : LivingType<T>(type, id, key, name, isAlive)
