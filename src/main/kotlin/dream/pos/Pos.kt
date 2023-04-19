@file:Suppress("NOTHING_TO_INLINE")

package dream.pos

import com.soywiz.kds.*
import com.soywiz.kmem.*
import dream.block.*
import dream.block.state.*
import dream.collision.*
import dream.entity.*
import dream.level.*
import dream.level.chunk.*
import dream.misc.*
import dream.nbt.types.*
import dream.pos.iterator.*
import dream.serializer.*
import dream.utils.*
import dream.utils.PI
import dream.utils.acos
import dream.utils.cos
import dream.utils.max
import dream.utils.min
import dream.utils.sin
import kotlinx.serialization.*
import kotlin.math.*

/**
 * Represents a Vector.
 *
 * Vector is a typealias for [Pos], since their are destinated to be equals.
 *
 * This is only for organization purporses.
 */
typealias Vec = Pos

/**
 * Represents a base immutable coordinate-system.
 */
@Open
@Serializable(PosSerializer::class)
public data class Pos(val x: Double, val y: Double, val z: Double) : Comparable<Pos> {
   
   constructor() : this(0.0, 0.0, 0.0)
   
   constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())
   
   constructor(codified: Long) : this(getX(codified), getY(codified), getZ(codified))
   
   constructor(entity: Entity) : this(entity.x, entity.y, entity.z)
   
   constructor(compound: CompoundTag) : this(compound.double("x"), compound.double("y"), compound.double("z"))
   
   constructor(tag: LongTag) : this(tag.value)
   
   /**
    * Gets if this pos is ZERO.
    */
   val isZero: Boolean get() = x == 0.0 && y == 0.0 && z == 0.0
   
   /**
    * Returns ``X`` position floored.
    *
    * Used to get the correct block on this position.
    */
   val flooredX get() = x.toIntFloor()
   
   /**
    * Returns ``Y`` position floored.
    *
    * Used to get the correct block on this position.
    */
   val flooredY get() = y.toIntFloor()
   
   /**
    * Returns ``Z`` position floored.
    *
    * Used to get the correct block on this position.
    */
   val flooredZ get() = z.toIntFloor()
   
   /**
    * Gets the centralized ``X`` pos.
    */
   val centralX: Double get() = x + 0.5
   
   /**
    * Gets the centralized ``Y`` pos.
    */
   val centralY: Double get() = y + 0.5
   
   /**
    * Gets the centralized ``Z`` pos.
    */
   val centralZ: Double get() = z + 0.5
   
   /**
    * Gets the lenght of this pos squared.
    */
   val lenghtSquared: Double get() = (x * x) + (y * y) + (z * z)
   
   /**
    * Gets the lenght of this pos.
    */
   val lenght: Double get() = sqrt(lenghtSquared)
   
   /**
    * Gets the nearest direction by this pos.
    */
   val nearestDirection: Direction get() = Direction.nearest(this)
   
   fun tileAt(level: Level) = level.getTileEntity(this)
   
   fun stateAtOrNull(level: Level) = level.getStateOrNull(this)
   fun stateAt(level: Level, default: IState = Blocks.AIR.state) = level.getState(this, default)
   fun setStateAt(level: Level, state: IState, flags: Int = 3) = level.setState(this, state, flags)
   
   fun blockAtOrNull(level: Level) = level.getBlockOrNull(this)
   fun blockAt(level: Level, default: Block = Blocks.AIR) = level.getBlock(this, default)
   fun setBlockAt(level: Level, block: Block, flags: Int = 3) = level.setBlock(this, block, flags)
   
   /**
    * Gets the chunk at this pos on [level].
    */
   fun chunkAt(level: Level): Chunk {
      return level.chunkAt(this)
   }
   
   /**
    * Gets a copied version of this pos.
    */
   fun copy(): Pos {
      return Pos(x, y, z)
   }
   
   /**
    * Gets a relative pos by [direction] with mod [mod].
    */
   fun relative(direction: Direction, mod: Double = 1.0): Pos {
      return if (mod == 0.0) {
         this
      } else {
         Pos(x + direction.stepX * mod, y + direction.stepY * mod, z + direction.stepZ * mod)
      }
   }
   
   /**
    * Gets a relative pos by [direction].
    */
   fun relative(direction: Direction): Pos {
      return Pos(x + direction.stepX, y + direction.stepY, z + direction.stepZ)
   }
   
   /**
    * Gets a relative pos by [axis] with mod [mod].
    */
   fun relative(axis: Axis, mod: Double = 1.0): Pos {
      return if (mod == 0.0) {
         this
      } else {
         val axisX = if (axis == Axis.X) mod else 0.0
         val axisY = if (axis == Axis.Y) mod else 0.0
         val axisZ = if (axis == Axis.Z) mod else 0.0
         Pos(x + axisX, y + axisY, z + axisZ)
      }
   }
   
   /**
    * Gets the above relative pos.
    */
   fun up(mod: Double = 1.0): Pos {
      return relative(Direction.UP, mod)
   }
   
   /**
    * Gets the below relative pos.
    */
   fun down(mod: Double = 1.0): Pos {
      return relative(Direction.DOWN, mod)
   }
   
   /**
    * Gets the north relative pos.
    */
   fun north(mod: Double = 1.0): Pos {
      return relative(Direction.NORTH, mod)
   }
   
   /**
    * Gets the south relative pos.
    */
   fun south(mod: Double = 1.0): Pos {
      return relative(Direction.SOUTH, mod)
   }
   
   /**
    * Gets the west relative pos.
    */
   fun west(mod: Double = 1.0): Pos {
      return relative(Direction.WEST, mod)
   }
   
   /**
    * Gets the east relative pos.
    */
   fun east(mod: Double = 1.0): Pos {
      return relative(Direction.EAST, mod)
   }
   
   /**
    * Gets an offset pos by the given coordinates.
    */
   fun offset(x: Double, y: Double, z: Double): Pos {
      return if (x == 0.0 && y == 0.0 && z == 0.0) {
         this
      } else {
         Pos(this.x + x, this.y + y, this.z + z)
      }
   }
   
   /**
    * Gets an offset pos by the given coordinates.
    */
   fun offset(x: Number, y: Number, z: Number): Pos {
      return offset(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Gets an offset pos by the given coordinates.
    */
   fun offset(pos: Pos): Pos {
      return offset(pos.x, pos.y, pos.z)
   }
   
   /**
    * Subtracts this pos by the given coordinates.
    */
   fun subtract(x: Double, y: Double, z: Double): Pos {
      return offset(-x, -y, -z)
   }
   
   /**
    * Subtracts this pos by the given coordinates.
    */
   fun subtract(x: Number, y: Number, z: Number): Pos {
      return subtract(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Subtracts this pos by the given coordinates.
    */
   fun subtract(pos: Pos): Pos {
      return subtract(pos.x, pos.y, pos.z)
   }
   
   /**
    * Multiplies this pos coordinates by [mod].
    */
   fun multiply(mod: Double): Pos {
      return when (mod) {
         1.0 -> this
         0.0 -> ZERO
         else -> Pos(x * mod, y * mod, z * mod)
      }
   }
   
   /**
    * Divides this pos coordinates by [mod].
    */
   fun divide(mod: Double): Pos {
      return when (mod) {
         1.0 -> this
         0.0 -> ZERO
         else -> Pos(x / mod, y / mod, z / mod)
      }
   }
   
   /**
    * Gets a copy of this pos at the given [y] coordinate.
    */
   fun atY(y: Double): Pos {
      return Pos(x, y, z)
   }
   
   /**
    * Rotates this pos by the given rotation.
    */
   fun rotate(rotation: Rotation): Pos {
      return when (rotation) {
         Rotation.CLOCKWISE -> Pos(-z, y, x)
         Rotation.BACK -> Pos(-x, y, -z)
         Rotation.COUNTER_CLOCKWISE -> Pos(z, y, -x)
         else -> this
      }
   }
   
   /**
    * Rotates this pos `X` coordinate by the given [angle] angle.
    *
    * Optionally you can decide the [cos] and [sin] data for the rotation.
    */
   fun rotateX(angle: Float, cos: Double = cos(angle), sin: Double = sin(angle)): Pos {
      val y = y * cos + z * sin
      val z = z * cos - this.y * sin
      return Pos(x, y, z)
   }
   
   /**
    * Rotates this pos `Y` coordinate by the given [angle] angle.
    *
    * Optionally you can decide the [cos] and [sin] data for the rotation.
    */
   fun rotateY(angle: Float, cos: Double = cos(angle), sin: Double = sin(angle)): Pos {
      val x = x * cos + z * sin
      val z = z * cos - this.x * sin
      return Pos(x, y, z)
   }
   
   /**
    * Rotates this pos `Z` coordinate by the given [angle] angle.
    *
    * Optionally you can decide the [cos] and [sin] data for the rotation.
    */
   fun rotateZ(angle: Float, cos: Double = cos(angle), sin: Double = sin(angle)): Pos {
      val x = x * cos + y * sin
      val y = y * cos - this.x * sin
      return Pos(x, y, z)
   }
   
   /**
    * Aligns this pos flooring their coordinates.
    */
   fun align(): Pos {
      return Pos(flooredX, flooredY, flooredZ)
   }
   
   /**
    * Aligns this pos flooring their coordinates only if in [axis].
    */
   fun align(vararg axis: Axis): Pos {
      val x = if (Axis.X in axis) flooredX else x
      val y = if (Axis.Y in axis) flooredY else y
      val z = if (Axis.Z in axis) flooredZ else z
      return Pos(x, y, z)
   }
   
   /**
    * Cross this pos with other [pos].
    */
   fun cross(pos: Pos): Pos {
      return Pos(
         y * pos.z - z * pos.y,
         z * pos.x - x * pos.z,
         x * pos.y - y * pos.x
      )
   }
   
   /**
    * Gets the angle between this pos and other [pos].
    */
   fun angle(pos: Pos): Double {
      return acos(dot(pos) / (lenght * pos.lenght))
   }
   
   /**
    * Gets the middle pos between this pos and other [pos].
    */
   fun middle(pos: Pos): Pos {
      return Pos(
         (x + pos.x) / 2,
         (y + pos.y) / 2,
         (z + pos.z) / 2
      )
   }
   
   /**
    * Returns if this pos intersects with min and max coordinates.
    */
   fun intersects(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
      return x in minX..maxX && y in minY..maxY && z in minZ..maxZ
   }
   
   /**
    * Returns if this pos intersects with min and max coordinates.
    */
   fun intersects(minX: Number, minY: Number, minZ: Number, maxX: Number, maxY: Number, maxZ: Number): Boolean {
      return intersects(
         minX.toDouble(),
         minY.toDouble(),
         minZ.toDouble(),
         maxX.toDouble(),
         maxY.toDouble(),
         maxZ.toDouble()
      )
   }
   
   /**
    * Returns if this pos intersects with [min] and [max].
    */
   fun intersects(min: Pos, max: Pos): Boolean {
      return intersects(min.x, min.y, min.z, max.x, max.y, max.z)
   }
   
   /**
    * Returns if this pos intersects with [box].
    */
   fun intersects(box: AABB): Boolean {
      return intersects(box.min, box.max)
   }
   
   /**
    * Returns if this pos intersects with [entity].
    */
   fun intersects(entity: Entity): Boolean {
      return intersects(entity.box)
   }
   
   /**
    * Returns if this pos intersects a sphere.
    */
   fun isInSphere(center: Pos, radius: Double): Boolean {
      return squared(center.x - x) + squared(center.y - y) + squared(center.z - z) <= radius
   }
   
   /**
    * Normalizes this pos.
    */
   fun normalize(with: Pos = this): Pos {
      val norm = 1.0 / with.lenght
      return Pos(with.x * norm, with.y * norm, with.z * norm)
   }
   
   /**
    * Gets an absolute version of this pos.
    */
   fun abs(): Pos {
      return Pos(abs(x), abs(y), abs(z))
   }
   
   /**
    * Gets the dot product by this pos and other [pos].
    */
   fun dot(pos: Pos): Double {
      return (x * pos.x) + (y * pos.y) + (z * pos.z)
   }
   
   /**
    * Gets the distance between this pos and given coordinates.
    */
   fun distance(x: Double, y: Double, z: Double): Double {
      return (this.x - x) + (this.y - y) + (this.z - z)
   }
   
   /**
    * Gets the distance between this pos and given coordinates.
    */
   fun distance(x: Number, y: Number, z: Number): Double {
      return distance(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Gets the distance between this pos and other [pos].
    */
   fun distance(pos: Pos): Double {
      return distance(pos.x, pos.y, pos.z)
   }
   
   /**
    * Gets the distance between this pos and entity pos.
    */
   fun distance(entity: Entity): Double {
      return distance(entity.x, entity.y, entity.z)
   }
   
   /**
    * Returns if this pos is near to the given coordinates by [radius].
    */
   fun isNear(x: Double, y: Double, z: Double, radius: Double): Boolean {
      return distance(x, y, z) <= radius
   }
   
   /**
    * Returns if this pos is near to the given coordinates by [radius].
    */
   fun isNear(x: Number, y: Number, z: Number, radius: Double): Boolean {
      return distance(x, y, z) <= radius
   }
   
   /**
    * Returns if this pos is near to other [pos] by [radius].
    */
   fun isNear(pos: Pos, radius: Double): Boolean {
      return distance(pos) <= radius
   }
   
   /**
    * Returns if this pos is near to [entity] by [radius].
    */
   fun isNear(entity: Entity, radius: Double): Boolean {
      return distance(entity) <= radius
   }
   
   /**
    * Returns if this pos is exact to the given coordinates.
    */
   fun exact(x: Double, y: Double, z: Double): Boolean {
      return this.x == x && this.y == y && this.z == z
   }
   
   /**
    * Returns if this pos is exact to the given coordinates.
    */
   fun exact(x: Number, y: Number, z: Number): Boolean {
      return exact(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Returns if this pos is exact to the given coordinates.
    */
   fun exact(x: Int, y: Int, z: Int): Boolean {
      return flooredX == x && flooredY == y && flooredZ == z
   }
   
   /**
    * Returns if this pos is exact to [pos].
    */
   fun exact(pos: Pos): Boolean {
      return exact(pos.x, pos.y, pos.z)
   }
   
   /**
    * Gets the limit count running through [amount] of times
    * in the given [direction] or until [predicate] returns true.
    */
   fun limit(direction: Direction, amount: Int, predicate: (Pos) -> Boolean): Int {
      var limit = 0
      
      val pos = mutable()
      while (limit <= amount && predicate(pos.move(direction))) {
         limit++
      }
      
      return limit
   }
   
   /**
    * Returns a new pos with x value equal to the second parameter
    * along the line between this pos and the passed pos, or null if not possible.
    */
   fun intermediateX(pos: Pos, value: Double): Pos? {
      val diffX = pos.x - x
      if (diffX * diffX < 1.0000000116860974E-7)
         return null
      
      val med = (value - x) / diffX
      if (med !in 0.0..1.0)
         return null
      
      return Pos(
         x + diffX * med,
         y + (pos.y - y) * med,
         z + (pos.z - z) * med
      )
   }
   
   /**
    * Returns a new pos with y value equal to the second parameter
    * along the line between this pos and the passed pos, or null if not possible.
    */
   fun intermediateY(pos: Pos, value: Double): Pos? {
      val diffY = pos.y - y
      if (diffY * diffY < 1.0000000116860974E-7)
         return null
         
      val med = (value - y) / diffY
      if (med !in 0.0..1.0)
         return null
      
      return Pos(
         x + (pos.x - x) * med,
         y + diffY * med,
         z + (pos.z - z) * med
      )
   }
   
   /**
    * Returns a new pos with z value equal to the second parameter
    * along the line between this pos and the passed pos, or null if not possible.
    */
   fun intermediateZ(pos: Pos, value: Double): Pos? {
      val diffZ = pos.z - z
      if (diffZ * diffZ < 1.0000000116860974E-7)
         return null
      
      val med = (value - z) / diffZ
      if (med !in 0.0..1.0)
         return null
      
      return Pos(
         x + (pos.x - x) * med,
         y + (pos.y - y) * med,
         z + diffZ * med
      )
   }
   
   /**
    * Gets a sequence of pos boxed from this pos (min) and other [pos] (max).
    */
   infix fun allInBox(pos: Pos): Sequence<Pos> {
      return BoxedPos(minOf(pos), maxOf(pos))
   }
   
   /**
    * Gets a min pos based on this pos and other.
    */
   fun minOf(pos: Pos): Pos {
      return copy(x = min(x, pos.x), y = min(y, pos.y), z = min(z, pos.z))
   }
   
   /**
    * Gets a max pos based on this pos and other.
    */
   fun maxOf(pos: Pos): Pos {
      return copy(x = max(x, pos.x), y = max(y, pos.y), z = max(z, pos.z))
   }
   
   /**
    * Gets an immutable pos based on this pos.
    */
   fun immutable(): Pos {
      return this
   }
   
   /**
    * Gets a mutable pos based on this pos.
    */
   fun mutable(): MutablePos {
      return MutablePos(x, y, z)
   }
   
   /**
    * Gets a loc based on this pos.
    */
   fun asLoc(level: Level, yaw: Float = 0f, pitch: Float = 0f): Loc {
      return Loc(level, x, y, z, yaw, pitch)
   }
   
   fun asLevelPos(level: Level) = LevelPos(level, x, y, z)
   
   /**
    * Gets a codified pos based on the floored coordinates of this pos.
    */
   fun asLong(): Long {
      return asLong(flooredX, flooredY, flooredZ)
   }
   
   fun toLongTag() = asLong().toTag()
   
   /**
    * Gets a boxed iterable with this pos as min and the given pos as max.
    */
   fun boxed(max: Pos): BoxIterator {
      return BoxIterator(this, max)
   }
   
   /**
    * Gets a randomly boxed iterable with this pos as min and the given pos as max.
    */
   fun randomBoxed(max: Pos): RandomBoxIterator {
      return RandomBoxIterator(this, max)
   }
   
   /**
    * Gets a step boxed iterable with this pos as min and the given pos as max.
    */
   fun stepBoxed(max: Pos): StepBoxIterator {
      return StepBoxIterator(this, max)
   }
   
   /**
    * Gets a boxed iterable with this pos as min and the given pos as max.
    */
   operator fun rangeTo(max: Pos): BoxIterator {
      return BoxIterator(this, max)
   }
   
   /**
    * Returns if this pos intersects [box].
    */
   operator fun contains(box: AABB): Boolean {
      return intersects(box)
   }
   
   override fun compareTo(other: Pos): Int {
      return x.compareTo(other.x) + y.compareTo(other.y) + z.compareTo(other.z)
   }
   
   override fun toString(): String {
      return "Pos(x=$x, y=$y, z=$z)"
   }
   
   override fun equals(other: Any?): Boolean {
      return if (other !is Pos) false else exact(other)
   }
   
   override fun hashCode(): Int {
      return hashCode(x, y, z)
   }
   
   companion object {
      internal val X_BITS = 1 + logBaseTwo(30000000.nextPowerOfTwo)
      internal val Z_BITS = X_BITS
      internal val Y_BITS = 64 - X_BITS - Z_BITS
      
      internal val X_MASK = (1L shl X_BITS) - 1
      internal val Y_MASK = (1L shl Y_BITS) - 1
      internal val Z_MASK = (1L shl Z_BITS) - 1
      
      internal val Y_SHIFT = Z_BITS
      internal val X_SHIFT = Y_SHIFT + Y_BITS
      
      /**
       * Zero position.
       */
      @JvmField val ZERO = Pos()
      
      /**
       * Decodes the X coordinate of a codified number.
       */
      fun getX(codified: Long): Int {
         return codified.toInt() shl 64 - X_SHIFT - X_BITS shr 64 - X_BITS
      }
      
      /**
       * Decodes the Y coordinate of a codified number.
       */
      fun getY(codified: Long): Int {
         return codified.toInt() shl 64 - Y_SHIFT - Y_BITS shr 64 - Y_BITS
      }
      
      /**
       * Decodes the Z coordinate of a codified number.
       */
      fun getZ(codified: Long): Int {
         return codified.toInt() shl 64 - Z_BITS shr 64 - Z_BITS
      }
      
      /**
       * Gets a codified coordinates as long represented by `X` `Y` `Z`
       */
      fun asLong(x: Long, y: Long, z: Long): Long {
         val maskedX = x and X_MASK shl X_SHIFT
         val maskedY = maskedX or y and Y_MASK shl Y_SHIFT
         return maskedY or z and Z_MASK shl 0
      }
      
      /**
       * Gets a codified coordinates as long represented by `X` `Y` `Z`
       */
      fun asLong(x: Number, y: Number, z: Number): Long {
         return asLong(x.toLong(), y.toLong(), z.toLong())
      }
      
      /**
       * Gets a pos by rotation of [yaw] and [pitch].
       */
      fun fromRotation(yaw: Float, pitch: Float): Pos {
         val cosYaw = cos(-yaw * 0.01745f - PI)
         val sinYaw = sin(-yaw * 0.01745f - PI)
         val cosPitch = cos(-pitch * 0.01745f)
         val sinPitch = sin(-pitch * 0.01745f)
         return Pos(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch)
      }
   }
}
