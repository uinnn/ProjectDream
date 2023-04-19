package dream.entity.type

import dream.*
import dream.entity.*
import kotlin.reflect.*

/**
 * Represents a entity type for animals.
 */
open class AnimalType<T : Animal>(
   type: KClass<T>,
   id: Int,
   key: Key,
   name: String,
   isAlive: Boolean,
) : CreatureType<T>(type, id, key, name, isAlive)
