package dream.pos

import com.soywiz.kmem.*
import dream.entity.*
import dream.nbt.*
import dream.nbt.types.*
import dream.serializer.*
import kotlinx.serialization.*

/**
 * Represents a base mutable coordinate-system.
 */
@Serializable(MutablePosSerializer::class)
public class MutablePos(
   override var x: Double,
   override var y: Double,
   override var z: Double,
) : Pos(x, y, z), CompoundStorable {
   
   constructor() : this(0.0, 0.0, 0.0)
   
   constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())
   
   constructor(codified: Long) : this(getX(codified), getY(codified), getZ(codified))
   
   constructor(entity: Entity) : this(entity.x, entity.y, entity.z)
   
   constructor(tag: CompoundTag) : this() {
      load(tag)
   }
   
   /**
    * Gets a copied version of this pos.
    */
   override fun copy(): MutablePos {
      return MutablePos(x, y, z)
   }
   
   /**
    * Sets the new coordinates of this pos.
    */
   fun set(x: Double, y: Double, z: Double): MutablePos {
      this.x = x
      this.y = y
      this.z = z
      return this
   }
   
   /**
    * Sets the new coordinates of this pos.
    */
   fun set(x: Number, y: Number, z: Number): MutablePos {
      return set(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Sets the new coordinates of this pos.
    */
   fun set(pos: Pos): MutablePos {
      return set(pos.x, pos.y, pos.z)
   }
   
   /**
    * Sets the new coordinates of this pos.
    */
   fun set(codified: Long): MutablePos {
      return set(getX(codified).toDouble(), getY(codified).toDouble(), getZ(codified).toDouble())
   }
   
   /**
    * Sets the new coordinates of this pos to ``0``.
    */
   fun zero(): MutablePos {
      return set(0.0, 0.0, 0.0)
   }
   
   /**
    * Sets the new `X` coordinate of this pos.
    */
   fun x(x: Double): MutablePos {
      this.x = x
      return this
   }
   
   /**
    * Sets the new `X` coordinate of this pos.
    */
   fun x(x: Number): MutablePos {
      return x(x.toDouble())
   }
   
   /**
    * Sets the new `Y` coordinate of this pos.
    */
   fun y(y: Double): MutablePos {
      this.y = y
      return this
   }
   
   /**
    * Sets the new `Y` coordinate of this pos.
    */
   fun y(y: Number): MutablePos {
      return y(y.toDouble())
   }
   
   /**
    * Sets the new `Z` coordinate of this pos.
    */
   fun z(z: Double): MutablePos {
      this.z = z
      return this
   }
   
   /**
    * Sets the new `Z` coordinate of this pos.
    */
   fun z(z: Number): MutablePos {
      return z(z.toDouble())
   }
   
   /**
    * Clamps this pos with axis by [min] and [max].
    */
   fun clamp(axis: Axis, min: Double, max: Double): MutablePos {
      return when (axis) {
         Axis.X -> set(x.clamp(min, max), y, z)
         Axis.Y -> set(x, y.clamp(min, max), z)
         Axis.Z -> set(x, y, z.clamp(min, max))
      }
   }
   
   /**
    * Moves this pos adding the given coordinates to this pos.
    */
   fun move(x: Double, y: Double, z: Double): MutablePos {
      return set(this.x + x, this.y + y, this.z + z)
   }
   
   /**
    * Moves this pos adding the given coordinates to this pos.
    */
   fun move(x: Number, y: Number, z: Number): MutablePos {
      return move(x.toDouble(), y.toDouble(), z.toDouble())
   }
   
   /**
    * Moves this pos adding the given coordinates to this pos.
    */
   fun move(pos: Pos): MutablePos {
      return move(pos.x, pos.y, pos.z)
   }
   
   /**
    * Moves this pos on the given direction with [mod].
    */
   fun move(direction: Direction, mod: Double = 1.0): MutablePos {
      return set(x + direction.stepX * mod, y + direction.stepY * mod, z + direction.stepZ * mod)
   }
   
   /**
    * Moves this pos above with [mod].
    */
   fun moveUp(mod: Double = 1.0): MutablePos {
      return move(Direction.UP, mod)
   }
   
   /**
    * Moves this pos below with [mod].
    */
   fun moveDown(mod: Double = 1.0): MutablePos {
      return move(Direction.DOWN, mod)
   }
   
   /**
    * Moves this pos to north with [mod].
    */
   fun moveNorth(mod: Double = 1.0): MutablePos {
      return move(Direction.NORTH, mod)
   }
   
   /**
    * Moves this pos to south with [mod].
    */
   fun moveSouth(mod: Double = 1.0): MutablePos {
      return move(Direction.SOUTH, mod)
   }
   
   /**
    * Moves this pos to west with [mod].
    */
   fun moveWest(mod: Double = 1.0): MutablePos {
      return move(Direction.WEST, mod)
   }
   
   /**
    * Moves this pos to east with [mod].
    */
   fun moveEast(mod: Double = 1.0): MutablePos {
      return move(Direction.EAST, mod)
   }
   
   /**
    * Gets an immutable pos based on this pos.
    */
   override fun immutable(): Pos {
      return Pos(x, y, z)
   }
   
   /**
    * Gets a mutable pos based on this pos.
    */
   override fun mutable(): MutablePos {
      return this
   }
   
   /**
    * Saves this pos to [tag].
    */
   override fun save(tag: CompoundTag) {
      tag["Pos"] = asLong()
   }
   
   /**
    * Loads this pos from [tag].
    */
   override fun load(tag: CompoundTag) {
      set(tag.long("Pos"))
   }
   
   override fun toString(): String {
      return "MutablePos(x=$x, y=$y, z=$z)"
   }
}
