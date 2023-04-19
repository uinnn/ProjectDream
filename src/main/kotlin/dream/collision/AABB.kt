package dream.collision

import com.soywiz.korma.geom.*
import dream.entity.*
import dream.level.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.pos.*
import dream.pos.iterator.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.*
import kotlin.math.*

/**
 * Represents an axis aligned bounding box.
 */
@Serializable(AABBSerializer::class)
@Open
data class AABB(val min: MutablePos, val max: MutablePos) : Iterable<Pos>, Comparable<AABB> {
   
   constructor() : this(Pos.ZERO, Pos.ZERO)
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(min: Pos, max: Pos) : this(min.mutable(), max.mutable())
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double)
      : this(MutablePos(minX, minY, minZ), MutablePos(maxX, maxY, maxZ))
   
   /**
    * Creates a AABB by the given values.
    */
   constructor(minX: Number, minY: Number, minZ: Number, maxX: Number, maxY: Number, maxZ: Number)
      : this(MutablePos(minX, minY, minZ), MutablePos(maxX, maxY, maxZ))
   
   /**
    * The minimum X position of this AABB.
    */
   var minX: Double
      get() = min.x
      set(value) {
         min.x = value
      }
   
   /**
    * The minimum Y position of this AABB.
    */
   var minY: Double
      get() = min.y
      set(value) {
         min.y = value
      }
   
   /**
    * The minimum Z position of this AABB.
    */
   var minZ: Double
      get() = min.z
      set(value) {
         min.z = value
      }
   
   /**
    * The maximum X position of this AABB.
    */
   var maxX: Double
      get() = max.x
      set(value) {
         max.x = value
      }
   
   /**
    * The maximum Y position of this AABB.
    */
   var maxY: Double
      get() = max.y
      set(value) {
         max.y = value
      }
   
   /**
    * The maximum Z position of this AABB.
    */
   var maxZ: Double
      get() = max.z
      set(value) {
         max.z = value
      }
   
   /**
    * Gets the floored min X of this box.
    */
   var blockMinX: Int
      get() = min.flooredX
      set(value) {
         min.x = value.toDouble()
      }
   
   /**
    * Gets the floored min Y of this box.
    */
   var blockMinY: Int
      get() = min.flooredY
      set(value) {
         min.y = value.toDouble()
      }
   
   /**
    * Gets the floored min Z of this box.
    */
   var blockMinZ: Int
      get() = min.flooredZ
      set(value) {
         min.z = value.toDouble()
      }
   
   /**
    * Gets the floored max X of this box.
    */
   var blockMaxX: Int
      get() = max.flooredX
      set(value) {
         max.x = value.toDouble()
      }
   
   /**
    * Gets the floored max Y of this box.
    */
   var blockMaxY: Int
      get() = max.flooredY
      set(value) {
         max.y = value.toDouble()
      }
   
   /**
    * Gets the floored max Z of this box.
    */
   var blockMaxZ: Int
      get() = max.flooredZ
      set(value) {
         max.z = value.toDouble()
      }
   
   /**
    * The volume of this AABB.
    */
   val volume get() = sizeX * sizeY * sizeZ
   
   /**
    * The size of this AABB as a point.
    */
   val size get() = Pos(sizeX, sizeY, sizeZ)
   
   /**
    * The size X position of this AABB.
    */
   val sizeX get() = maxX - minX
   
   /**
    * The size Y position of this AABB.
    */
   val sizeY get() = maxY - minY
   
   /**
    * The size Z position of this AABB.
    */
   val sizeZ get() = maxZ - minZ
   
   /**
    * Gets the length squared of this AABB.
    */
   val lengthSquared get() = max.lenghtSquared - min.lenghtSquared
   
   /**
    * Gets the length of this AABB.
    */
   val length get() = sqrt(lengthSquared)
   
   /**
    * Gets the range between the [minX] and [maxX]
    */
   val rangeX get() = minX..maxX
   
   /**
    * Gets the range between the [minY] and [maxY]
    */
   val rangeY get() = minY..maxY
   
   /**
    * Gets the range between the [minZ] and [maxZ]
    */
   val rangeZ get() = minZ..maxZ
   
   /**
    * Gets the average edge length of this AABB
    */
   val averageEdgeLength get() = (sizeX + sizeY + sizeZ) / 3
   
   /**
    * Expands this AABB by the given coordinates.
    */
   fun add(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): AABB {
      min.move(minX, minY, minZ)
      max.move(maxX, maxY, maxZ)
      return this
   }
   
   /**
    * Creates a copy of this AABB expanded by the given coordinates.
    */
   fun added(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): AABB {
      return copy().add(minX, minY, minZ, maxX, maxY, maxZ)
   }
   
   /**
    * Expands this AABB by the given [radius].
    *
    * This is, the AABB will be bigger.
    */
   fun expand(radius: Double): AABB {
      return add(-radius, -radius, -radius, radius, radius, radius)
   }
   
   /**
    * Creates a copy of this AABB expanded by the given [radius].
    *
    * This is, the AABB will be bigger.
    */
   fun expanded(radius: Double): AABB {
      return copy().expand(radius)
   }
   
   /**
    * Expands the min position of this AABB.
    */
   fun expandMin(x: Double, y: Double, z: Double): AABB {
      min.move(x, y, z)
      return this
   }
   
   /**
    * Expands the min position of this AABB in a copy.
    */
   fun expandedMin(x: Double, y: Double, z: Double): AABB {
      return copy().expandMin(x, y, z)
   }
   
   /**
    * Expands the max position of this AABB.
    */
   fun expandMax(x: Double, y: Double, z: Double): AABB {
      max.move(x, y, z)
      return this
   }
   
   /**
    * Expands the max position of this AABB in a copy.
    */
   fun expandedMax(x: Double, y: Double, z: Double): AABB {
      return copy().expandMax(x, y, z)
   }
   
   /**
    * Contracts this AABB by the given coordinates.
    */
   fun subtract(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): AABB {
      min.move(-minX, -minY, -minZ)
      max.move(-maxX, -maxY, -maxZ)
      return this
   }
   
   /**
    * Creates a copy of this AABB contracted by the given coordinates.
    */
   fun subtracted(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): AABB {
      return copy().subtract(minX, minY, minZ, maxX, maxY, maxZ)
   }
   
   /**
    * Contracts this AABB by the given [radius].
    *
    * This is, the AABB will be smaller.
    */
   fun contract(radius: Double): AABB {
      min.move(radius, radius, radius)
      max.move(-radius, -radius, -radius)
      return this
   }
   
   /**
    * Creates a copy of this AABB contracted by the given [radius].
    *
    * This is, the AABB will be smaller.
    */
   fun contracted(radius: Double): AABB {
      return copy().contract(radius)
   }
   
   /**
    * Expands the min position of this AABB.
    */
   fun contractMin(x: Double, y: Double, z: Double): AABB {
      min.move(-x, -y, -z)
      return this
   }
   
   /**
    * Expands the min position of this AABB in a copy.
    */
   fun contractedMin(x: Double, y: Double, z: Double): AABB {
      return copy().contractMin(x, y, z)
   }
   
   /**
    * Expands the max position of this AABB.
    */
   fun contractMax(x: Double, y: Double, z: Double): AABB {
      max.move(-x, -y, -z)
      return this
   }
   
   /**
    * Expands the max position of this AABB in a copy.
    */
   fun contractedMax(x: Double, y: Double, z: Double): AABB {
      return copy().contractMax(x, y, z)
   }
   
   /**
    * Clamps this AABB by axis.
    */
   fun clamp(axis: Axis, min: Double, max: Double): AABB {
      this.min.clamp(axis, min, max)
      this.max.clamp(axis, min, max)
      return this
   }
   
   /**
    * Creates a copy of this AABB clamped by axis.
    */
   fun clamped(axis: Axis, min: Double, max: Double): AABB {
      return copy().clamp(axis, min, max)
   }
   
   /**
    * Sets this AABB as the provided pos.
    */
   fun set(min: Pos, max: Pos): AABB {
      this.min.set(min)
      this.max.set(max)
      return this
   }
   
   /**
    * Sets this AABB as the provided pos.
    */
   fun set(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): AABB {
      min.set(minX, minY, minZ)
      max.set(maxX, maxY, maxZ)
      return this
   }
   
   /**
    * Moves this AABB with given mod's.
    */
   fun move(x: Double, y: Double, z: Double): AABB {
      min.move(x, y, z)
      max.move(x, y, z)
      return this
   }
   
   /**
    * Moves this AABB with given pos.
    */
   fun move(pos: Pos): AABB {
      min.move(pos)
      max.move(pos)
      return this
   }
   
   /**
    * Moves this AABB with given direction with [mod].
    */
   fun move(direction: Direction, mod: Double = 1.0): AABB {
      min.move(direction, mod)
      max.move(direction, mod)
      return this
   }
   
   /**
    * Gets an offset of this AABB with given mod's.
    */
   fun offset(x: Double, y: Double, z: Double): AABB {
      return copy().move(x, y, z)
   }
   
   /**
    * Gets an offset of this AABB with given pos.
    */
   fun offset(pos: Pos): AABB {
      return copy().move(pos)
   }
   
   /**
    * Gets an offset of this AABB with given direction with [mod].
    */
   fun offset(direction: Direction, mod: Double = 1.0): AABB {
      return copy().move(direction, mod)
   }
   
   /**
    * Calculates the offset `X` of this AABB to [other] AABB with [offset].
    *
    * If this AABB and [other] AABB intersects their `Y` and `Z`
    * calculates and returns the offset between them in `X` coordinates.
    */
   fun calculateOffsetX(other: AABB, offset: Double): Double {
      if (!intersectsY(other.minY, other.maxY) && !intersectsZ(other.minZ, other.maxZ)) {
         return offset
      }
      
      var n = offset
      if (n > 0.0 && other.maxX <= minX) {
         val x = minX - other.maxX
         if (x < n) {
            n = x
         }
      } else if (n < 0.0 && other.minX >= maxX) {
         val x = maxX - other.minX
         if (x > n) {
            n = x
         }
      }
      
      return n
   }
   
   /**
    * Calculates the offset `Y` of this AABB to [other] AABB with [offset].
    *
    * If this AABB and [other] AABB intersects their `X` and `Z`
    * calculates and returns the offset between them in `Y` coordinates.
    */
   fun calculateOffsetY(other: AABB, offset: Double): Double {
      if (!intersectsX(other.minX, other.maxX) && !intersectsZ(other.minZ, other.maxZ)) {
         return offset
      }
      
      var n = offset
      if (n > 0.0 && other.maxY <= minY) {
         val y = minY - other.maxY
         if (y < n) {
            n = y
         }
      } else if (n < 0.0 && other.minY >= maxY) {
         val y = maxY - other.minY
         if (y > n) {
            n = y
         }
      }
      
      return n
   }
   
   /**
    * Calculates the offset `Z` of this AABB to [other] AABB with [offset].
    *
    * If this AABB and [other] AABB intersects their `X` and `Y`
    * calculates and returns the offset between them in `Z` coordinates.
    */
   fun calculateOffsetZ(other: AABB, offset: Double): Double {
      if (!intersectsX(other.minX, other.maxX) && !intersectsY(other.minY, other.maxY)) {
         return offset
      }
      
      var n = offset
      if (n > 0.0 && other.maxZ <= minZ) {
         val z = minZ - other.maxZ
         if (z < n) {
            n = z
         }
      } else if (n < 0.0 && other.minZ >= maxZ) {
         val z = maxZ - other.minZ
         if (z > n) {
            n = z
         }
      }
      
      return n
   }
   
   /**
    * Gets all intersected entities on this AABB.
    */
   fun intersectedEntities(level: Level): List<Entity> {
      return level.getEntitiesIntersected(this)
   }
   
   /**
    * Gets all intersected living entities on this AABB.
    */
   fun intersectedLivingEntities(level: Level): Sequence<EntityLiving> {
      return level.getLivingEntitiesIntersected(this)
   }
   
   /**
    * Gets all intersected entities on this AABB.
    */
   fun intersectedCreatures(level: Level): Sequence<Creature> {
      return level.getCreaturesIntersected(this)
   }
   
   /**
    * Gets if this AABB intersects the `X` position.
    */
   fun intersectsX(minX: Double, maxX: Double): Boolean {
      return this.maxX >= minX && this.minX <= maxX
   }
   
   /**
    * Gets if this AABB intersects the `Y` position.
    */
   fun intersectsY(minY: Double, maxY: Double): Boolean {
      return this.maxY >= minY && this.minY <= maxY
   }
   
   /**
    * Gets if this AABB intersects the `Z` position.
    */
   fun intersectsZ(minZ: Double, maxZ: Double): Boolean {
      return this.maxZ >= minZ && this.minZ <= maxZ
   }
   
   /**
    * Gets if this AABB intersects the given positions.
    */
   fun intersects(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
      return intersectsX(minX, maxX) && intersectsY(minY, maxY) && intersectsZ(minZ, maxZ)
   }
   
   /**
    * Gets if this AABB intersects the given positions.
    */
   fun intersects(min: Pos, max: Pos): Boolean {
      return intersects(min.x, min.y, min.z, max.x, max.y, max.z)
   }
   
   /**
    * Gets if this AABB intersects [other] box.
    */
   fun intersects(other: AABB): Boolean {
      return intersects(other.min, other.max)
   }
   
   /**
    * Gets if this AABB intersects entity.
    */
   fun intersects(entity: Entity): Boolean {
      return intersects(entity.box)
   }
   
   /**
    * Gets if this AABB intersects a sphere.
    */
   fun isInSphere(origin: Pos, radius: Double): Boolean {
      return min.isInSphere(origin, radius) || max.isInSphere(origin, radius)
   }
   
   /**
    * Returns if coordinates is inside of this AABB.
    */
   fun isInside(x: Double, y: Double, z: Double): Boolean {
      return x in rangeX || y in rangeY || z in rangeZ
   }
   
   /**
    * Returns if [pos] is inside of this AABB.
    */
   fun isInside(pos: Pos): Boolean {
      return isInside(pos.x, pos.y, pos.z)
   }
   
   /**
    * Returns if entity is inside of this AABB.
    */
   fun isInside(entity: Entity): Boolean {
      return isInside(entity.x, entity.y, entity.z)
   }
   
   /**
    * Gets if this AABB intersects other AABB.
    */
   operator fun contains(other: AABB): Boolean {
      return intersects(other)
   }
   
   /**
    * Gets a box iterator for this AABB.
    */
   fun box(): BoxIterator {
      return BoxIterator(min, max)
   }
   
   /**
    * Gets a step box iterator for this AABB.
    */
   fun stepBox(): StepBoxIterator {
      return StepBoxIterator(min, max)
   }
   
   /**
    * Gets a random box iterator for this AABB.
    */
   fun randomBox(): RandomBoxIterator {
      return RandomBoxIterator(min, max)
   }
   
   /**
    * Save the data of this AABB to compound tag.
    */
   fun toCompound(): CompoundTag {
      return compound {
         set("Min", min.asLong())
         set("Max", max.asLong())
      }
   }
   
   override fun compareTo(other: AABB): Int {
      return volume.compareTo(other.volume)
   }
   
   override fun iterator(): BoxIterator {
      return BoxIterator(min, max)
   }
   
   companion object {
      @JvmField val ZERO = AABB(0, 0, 0, 0, 0, 0)
      
      /**
       * Creates a AABB from [tag] created by [AABB.toCompound].
       */
      fun fromCompound(tag: CompoundTag): AABB {
         val min = Pos(tag.long("Min"))
         val max = Pos(tag.long("Max"))
         return AABB(min, max)
      }
   }
   
   
}
