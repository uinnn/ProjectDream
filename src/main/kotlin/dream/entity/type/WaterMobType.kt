package dream.entity.type

import dream.*
import dream.entity.*
import kotlin.reflect.*

/**
 * Represents a entity type for water creatures.
 */
open class WaterMobType<T : WaterMob>(
   type: KClass<T>,
   id: Int,
   key: Key,
   name: String,
   isAlive: Boolean,
) : LivingType<T>(type, id, key, name, isAlive)
