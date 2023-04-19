package dream.entity.type

import dream.*
import dream.entity.*
import kotlin.reflect.*

/**
 * Represents a entity type for creatures.
 */
open class LivingType<T : EntityLiving>(
   type: KClass<T>,
   id: Int,
   key: Key,
   name: String,
   val isAlive: Boolean = false,
) : EntityType<T>(type, id, key, name)
