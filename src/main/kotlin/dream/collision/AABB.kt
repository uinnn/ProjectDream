package dream.collision

import dream.entity.base.*
import dream.level.*
import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.pos.*
import dream.serializer.*
import dream.utils.*
import kotlinx.serialization.*
import kotlin.math.*

typealias Region = AABB

/**
 * Represents an axis aligned bounding box.
 */
@Serializable(AABBSerializer::class)
@Open
data class AABB(val min: MutablePos, val max: MutablePos) : Sequence<Pos>, Comparable<AABB>, LongArrayStorable {
  
  constructor() : this(Pos.ZERO, Pos.ZERO)
  
  /**
   * Creates a AABB by the given values.
   */
  constructor(min: Pos, max: Pos) : this(min.mutable(), max.mutable())
  
  /**
   * Creates a AABB by the given values.
   */
  constructor(
    minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double
  ) : this(MutablePos(minX, minY, minZ), MutablePos(maxX, maxY, maxZ))
  
  /**
   * Creates a AABB by the given values.
   */
  constructor(
    minX: Number, minY: Number, minZ: Number, maxX: Number, maxY: Number, maxZ: Number
  ) : this(MutablePos(minX, minY, minZ), MutablePos(maxX, maxY, maxZ))
  
  constructor(minCodified: Long, maxCodified: Long) : this(MutablePos(minCodified), MutablePos(maxCodified))
  
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
   * Calculates the center X coordinate based on the minimum and maximum X values.
   * @return The center X coordinate.
   */
  val centerX get() = (minX + maxX) / 2
  
  /**
   * Calculates the center Y coordinate based on the minimum and maximum Y values.
   * @return The center Y coordinate.
   */
  val centerY get() = (minY + maxY) / 2
  
  /**
   * Calculates the center Z coordinate based on the minimum and maximum Z values.
   * @return The center Z coordinate.
   */
  val centerZ get() = (minZ + maxZ) / 2
  
  /**
   * The center position of a bounding box.
   */
  val center get() = Pos(centerX, centerY, centerZ)
  
  /**
   * The size of this AABB as a point.
   */
  val size get() = Pos(sizeX, sizeY, sizeZ)
  
  /**
   * Calculates and returns the size of the AABB in the X-axis.
   *
   * @return The size of the AABB in the X-axis.
   */
  val sizeX: Double
    get() = abs(maxX - minX)
  
  /**
   * Calculates and returns the width of the AABB.
   *
   * This is equals to [sizeX].
   *
   * @return The width of the AABB.
   */
  val width: Double
    get() = sizeX
  
  /**
   * Calculates and returns the size of the AABB in the Y-axis.
   *
   * @return The size of the AABB in the Y-axis.
   */
  val sizeY: Double
    get() = abs(maxY - minY)
  
  /**
   * Calculates and returns the height of the AABB.
   *
   * This is equals to [sizeY].
   *
   * @return The height of the AABB.
   */
  val height: Double
    get() = sizeY
  
  /**
   * Calculates and returns the size of the AABB in the Z-axis.
   *
   * @return The size of the AABB in the Z-axis.
   */
  val sizeZ: Double
    get() = abs(maxZ - minZ)
  
  /**
   * Calculates and returns the depth of the AABB.
   *
   * This is equals to [sizeZ].
   *
   * @return The depth of the AABB.
   */
  val depth: Double
    get() = sizeZ
  
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
   * Gets a list of the corners of this AABB.
   */
  val corners
    get() = listOf(
      Pos(minX, minY, minZ),
      Pos(minX, maxY, minZ),
      Pos(maxX, maxY, minZ),
      Pos(maxX, minY, minZ),
      Pos(minX, minY, maxZ),
      Pos(minX, maxY, maxZ),
      Pos(maxX, maxY, maxZ),
      Pos(maxX, minY, maxZ)
    )
  
  /**
   * Generates a random value within the range of minX and maxX.
   *
   * @return A randomly generated value within the X range.
   */
  val randomX: Int
    get() = randomInt(min.flooredX, max.flooredX)
  
  /**
   * Generates a random value within the range of minY and maxY.
   *
   * @return A randomly generated value within the Y range.
   */
  val randomY: Int
    get() = randomInt(min.flooredY, max.flooredY)
  
  /**
   * Generates a random value within the range of minZ and maxZ.
   *
   * @return A randomly generated value within the Z range.
   */
  val randomZ: Int
    get() = randomInt(min.flooredZ, max.flooredZ)
  
  /**
   * Generates a random position.
   *
   * @return A randomly generated position.
   */
  fun randomPos(): Pos = Pos(randomX, randomY, randomZ)
  
  /**
   * Checks if the AABB is empty.
   *
   * @return `true` if the AABB is empty, `false` otherwise.
   */
  fun isEmpty(): Boolean = min >= max
  
  /**
   * Checks if the AABB is not empty.
   *
   * @return `true` if the AABB is not empty, `false` otherwise.
   */
  fun isNotEmpty(): Boolean = !isEmpty()
  
  /**
   * Checks if the AABB is a square.
   *
   * @return `true` if the AABB is a square, `false` otherwise.
   */
  fun isSquare(): Boolean {
    val height = height
    return width == height && height == depth
  }
  
  /**
   * Checks if the AABB is a cube.
   *
   * @return `true` if the AABB is a cube, `false` otherwise.
   */
  fun isCube(): Boolean {
    val width = width
    val height = height
    val depth = depth
    return width == height && depth == height && depth == width
  }
  
  /**
   * Computes the intersection between this AABB and another AABB.
   *
   * @param other The other AABB to compute the intersection with.
   * @return The intersection AABB if it exists, `null` otherwise.
   */
  fun intersect(other: AABB): AABB? {
    val intersectMinX = maxOf(minX, other.minX)
    val intersectMaxX = minOf(maxX, other.maxX)
    val intersectMinY = maxOf(minY, other.minY)
    val intersectMaxY = minOf(maxY, other.maxY)
    val intersectMinZ = maxOf(minZ, other.minZ)
    val intersectMaxZ = minOf(maxZ, other.maxZ)
    
    return if (intersectMinX <= intersectMaxX && intersectMinY <= intersectMaxY && intersectMinZ <= intersectMaxZ) {
      AABB(intersectMinX, intersectMaxX, intersectMinY, intersectMaxY, intersectMinZ, intersectMaxZ)
    } else {
      null
    }
  }
  
  /**
   * Checks if this AABB is adjacent to another AABB.
   *
   * @param other The other AABB to check adjacency with.
   * @return `true` if the AABBs are adjacent, `false` otherwise.
   */
  fun isAdjacentTo(other: AABB): Boolean {
    val intersect = intersect(other)
    return intersect != null && intersect.isNotEmpty()
  }
  
  /**
   * Checks if the center of this AABB is inside another AABB.
   *
   * @param other The other AABB to check containment with.
   * @return `true` if the center of this AABB is inside the other AABB, `false` otherwise.
   */
  fun isCenterInside(other: AABB): Boolean {
    return other.isInside(center)
  }
  
  /**
   * Scales the AABB by a given factor.
   *
   * @param factor The scaling factor.
   * @return The scaled AABB.
   */
  fun scale(factor: Double): AABB {
    val scaledWidth = width * factor
    val scaledHeight = height * factor
    val scaledDepth = depth * factor
    val center = center
    
    return AABB(
      center.x - scaledWidth / 2.0,
      center.x + scaledWidth / 2.0,
      center.y - scaledHeight / 2.0,
      center.y + scaledHeight / 2.0,
      center.z - scaledDepth / 2.0,
      center.z + scaledDepth / 2.0
    )
  }
  
  /**
   * Merges this AABB with another AABB.
   *
   * @param other The other AABB to merge with.
   * @return The merged AABB.
   */
  fun merge(other: AABB): AABB {
    val mergedMinX = minOf(minX, other.minX)
    val mergedMaxX = maxOf(maxX, other.maxX)
    val mergedMinY = minOf(minY, other.minY)
    val mergedMaxY = maxOf(maxY, other.maxY)
    val mergedMinZ = minOf(minZ, other.minZ)
    val mergedMaxZ = maxOf(maxZ, other.maxZ)
    
    return AABB(mergedMinX, mergedMaxX, mergedMinY, mergedMaxY, mergedMinZ, mergedMaxZ)
  }
  
  /**
   * Computes the union of a list of AABBs.
   *
   * @param boxes The list of AABBs to compute the union of.
   * @return The union AABB.
   */
  fun union(boxes: List<AABB>): AABB {
    var result = this
    boxes.forEach { region -> result = result.merge(region) }
    return result
  }
  
  /**
   * Gets the relative position of a point within the AABB.
   *
   * @param pos The point to compute the relative position of.
   * @return The relative position of the point within the AABB.
   */
  fun getRelative(pos: Pos): Pos {
    return Pos(pos.x - minX, pos.y - minY, pos.z - minZ)
  }
  
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
    val dx = center.x - center.x.coerceIn(minX, maxX)
    val dy = center.y - center.y.coerceIn(minY, maxY)
    val dz = center.z - center.z.coerceIn(minZ, maxZ)
    return (dx * dx) + (dy * dy) + (dz * dz) <= radius * radius
  }
  
  /**
   * Returns if coordinates is inside of this AABB.
   */
  fun isInside(x: Double, y: Double, z: Double): Boolean {
    return x in rangeX && y in rangeY && z in rangeZ
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
  fun box() = min.allInBox(max)
  
  override fun save(tag: LongArrayTag) {
    tag.add(min.asLong())
    tag.add(max.asLong())
  }
  
  override fun load(tag: LongArrayTag) {
    min.set(tag.getLong(0))
    max.set(tag.getLong(1))
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
  
  /**
   * The iterator is lazily (sequence iterator)
   */
  override fun iterator(): Iterator<Pos> {
    return box().iterator()
  }
  
  companion object {
    @JvmField
    val ZERO = AABB(0, 0, 0, 0, 0, 0)
    
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
