package dream.entity.type

import dream.*
import dream.entity.projectile.*
import kotlin.reflect.*

/**
 * Represents a entity type for projectiles.
 */
open class ProjectileType<T : Projectile>(
   type: KClass<T>,
   id: Int,
   key: Key,
   name: String,
) : EntityType<T>(type, id, key, name)
