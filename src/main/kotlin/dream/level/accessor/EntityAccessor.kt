package dream.level.accessor

import dream.collision.*
import dream.entity.*
import dream.entity.player.*
import dream.pos.*
import dream.tiles.*
import dream.utils.*
import java.util.*

/**
 * Represents an acessor for entities.
 */
interface EntityAccessor {
   
   /**
    * Gets all players.
    */
   val players: MutableList<Player>
   
   /**
    * Gets all entities mapped by their serial id.
    */
   val entitiesById: IntObjectMap<Entity>
   
   /**
    * Gets all entities mapped by their unique id.
    */
   val entitiesByUUID: MutableMap<UUID, Entity>
   
   /**
    * Gets all tile entities loaded mapped by their position.
    */
   val tileEntitiesByPos: MutableMap<Pos, Tile>
   
   /**
    * Returns all tile entities loaded on this world.
    */
   val tileEntities: List<Tile>
      get() = tileEntitiesByPos.values.toList()
   
   /**
    * Returns all entities of this world.
    */
   val entities: List<Entity>
      get() = entitiesById.values.toList()
   
   /**
    * Returns all living entities of this world
    */
   val livingEntities: List<EntityLiving>
      get() = entitiesById.values.filterIsInstance<EntityLiving>()
   
   /**
    * Returns all creature entities of this world
    */
   val creatures: List<Creature>
      get() = entitiesById.values.filterIsInstance<Creature>()
   
   /**
    * Gets a tile entity by their position.
    */
   fun getTileEntity(point: Pos): Tile? {
      return tileEntitiesByPos[point]
   }
   
   /**
    * Gets a tile entity by the given positions.
    */
   fun getTileEntity(x: Int, y: Int, z: Int): Tile? {
      return getTileEntity(Pos(x, y, z))
   }
   
   /**
    * Gets an entity by their serial id.
    */
   fun getEntity(id: Int): Entity? {
      return entitiesById.get(id)
   }
   
   /**
    * Gets an entity by their unique id.
    */
   fun getEntity(uuid: UUID): Entity? {
      return entitiesByUUID[uuid]
   }
   
   /**
    * Gets a living entity by their serial id.
    */
   fun getLivingEntity(id: Int): EntityLiving? {
      return getEntity<EntityLiving>(id)
   }
   
   /**
    * Gets a living entity by their unique id.
    */
   fun getLivingEntity(uuid: UUID): EntityLiving? {
      return getEntity<EntityLiving>(uuid)
   }
   
   /**
    * Gets a creature entity by their serial id.
    */
   fun getCreature(id: Int): Creature? {
      return getEntity<Creature>(id)
   }
   
   /**
    * Gets a creature entity by their unique id.
    */
   fun getCreature(uuid: UUID): Creature? {
      return getEntity<Creature>(uuid)
   }
   
   /**
    * Gets all entities intersected by [box].
    */
   fun getEntitiesIntersected(box: AABB): List<Entity> {
      return entitiesById.values.filter { box.intersects(it) }
   }
   
   /**
    * Gets all entities intersected by [min] and [max] pos.
    */
   fun getEntitiesIntersected(min: Pos, max: Pos): List<Entity> {
      return getEntitiesIntersected(AABB(min, max))
   }
   
   /**
    * Gets all entities intersected by [min] and [max] pos.
    */
   fun getEntitiesAround(center: Pos, x: Double, y: Double, z: Double): List<Entity> {
      return getEntitiesIntersected(
         AABB(
            center.x - x,
            center.y - y,
            center.z - z,
            center.x + x,
            center.y + y,
            center.z + z
         )
      )
   }
   
   /**
    * Gets all living entities intersected by [box].
    */
   fun getLivingEntitiesIntersected(box: AABB): Sequence<EntityLiving> {
      return getEntitiesIntersected<EntityLiving>(box)
   }
   
   /**
    * Gets all living entities intersected by [box].
    */
   fun getCreaturesIntersected(box: AABB): Sequence<Creature> {
      return getEntitiesIntersected<Creature>(box)
   }
}

/**
 * Gets all living entities intersected by [box].
 */
@JvmName("getEntitiesIntersectedTyped")
inline fun <reified T> EntityAccessor.getEntitiesIntersected(box: AABB): Sequence<T> {
   return entitiesById.values
      .asSequence()
      .filter { box.intersects(it) }
      .filterIsInstance<T>()
}

/**
 * Gets all entities intersected by [min] and [max] pos.
 */
@JvmName("getEntitiesIntersectedTyped")
inline fun <reified T> EntityAccessor.getEntitiesIntersected(min: Pos, max: Pos): Sequence<T> {
   return getEntitiesIntersected<T>(AABB(min, max))
}

/**
 * Gets all entities intersected by [min] and [max] pos.
 */
@JvmName("getEntitiesAroundTyped")
inline fun <reified T> EntityAccessor.getEntitiesAround(center: Pos, x: Double, y: Double, z: Double): Sequence<T> {
   return getEntitiesIntersected<T>(
      AABB(
         center.x - x,
         center.y - y,
         center.z - z,
         center.x + x,
         center.y + y,
         center.z + z
      )
   )
}

/**
 * Gets an entity by their unique id.
 */
inline fun <reified T> EntityAccessor.getEntity(uuid: UUID): T? {
   return entitiesByUUID[uuid] as? T
}

/**
 * Gets an entity by their serial id.
 */
inline fun <reified T> EntityAccessor.getEntity(id: Int): T? {
   return entitiesById.get(id) as? T
}
