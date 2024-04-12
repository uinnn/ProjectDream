package dream.pos

import dream.api.*
import dream.utils.*
import korlibs.datastructure.random.fastRandom
import kotlin.math.*

/**
 * Represents a direction.
 */
enum class Direction(
  override val id: String,
  val axis: Axis,
  val direction: AxisDirection,
  val stepX: Double,
  val stepY: Double,
  val stepZ: Double,
) : Id {
  
  DOWN("down", Axis.Y, AxisDirection.NEGATIVE, 0.0, -1.0, 0.0),
  UP("up", Axis.Y, AxisDirection.POSITIVE, 0.0, 1.0, 0.0),
  NORTH("north", Axis.Z, AxisDirection.NEGATIVE, 0.0, 0.0, -1.0),
  SOUTH("south", Axis.Z, AxisDirection.POSITIVE, 0.0, 0.0, 1.0),
  WEST("west", Axis.X, AxisDirection.NEGATIVE, -1.0, 0.0, 0.0),
  EAST("east", Axis.X, AxisDirection.POSITIVE, 1.0, 0.0, 0.0);
  
  /**
   * The step as Pos of this direction.
   */
  val step = Pos(stepX, stepY, stepZ)
  
  /**
   * Gets the horizontal index of this direction.
   */
  val horizontalIndex: Int
    get() = when (this) {
      DOWN, UP -> -1
      SOUTH -> 0
      WEST -> 1
      NORTH -> 2
      EAST -> 3
    }
  
  /**
   * Returns the offset X of this facing.
   */
  val offsetX: Int get() = if (axis == Axis.X) direction.offset else 0
  
  /**
   * Returns the offset Y of this facing.
   */
  val offsetY: Int get() = if (axis == Axis.Y) direction.offset else 0
  
  /**
   * Returns the offset Z of this facing.
   */
  val offsetZ: Int get() = if (axis == Axis.Z) direction.offset else 0
  
  /**
   * Returns the opposite direction.
   */
  fun opposite(): Direction {
    return when (this) {
      UP -> DOWN
      DOWN -> UP
      SOUTH -> NORTH
      NORTH -> SOUTH
      WEST -> EAST
      EAST -> WEST
    }
  }
  
  /**
   * Rotates this direction by [rotation].
   */
  fun rotate(rotation: Rotation): Direction {
    return if (axis == Axis.Y) {
      this
    } else {
      when (rotation) {
        Rotation.CLOCKWISE -> rotateClockWiseY()
        Rotation.BACK -> opposite()
        Rotation.COUNTER_CLOCKWISE -> rotateClockWiseY()
        else -> this
      }
    }
  }
  
  /**
   * Rotates this direction `X` axis clockwise.
   */
  fun rotateClockWiseX(): Direction {
    return when (this) {
      NORTH -> DOWN
      SOUTH -> UP
      UP -> NORTH
      DOWN -> SOUTH
      else -> this
    }
  }
  
  /**
   * Rotates this direction `X` axis counterclockwise.
   */
  fun rotateCounterClockWiseX(): Direction {
    return when (this) {
      DOWN -> NORTH
      UP -> SOUTH
      NORTH -> UP
      SOUTH -> DOWN
      else -> this
    }
  }
  
  /**
   * Rotates this direction `Z` axis clockwise.
   */
  fun rotateClockWiseZ(): Direction {
    return when (this) {
      DOWN -> WEST
      UP -> EAST
      WEST -> UP
      EAST -> DOWN
      else -> this
    }
  }
  
  /**
   * Rotates this direction `Z` axis counterclockwise.
   */
  fun rotateCounterClockWiseZ(): Direction {
    return when (this) {
      DOWN -> EAST
      UP -> WEST
      WEST -> DOWN
      EAST -> UP
      else -> this
    }
  }
  
  /**
   * Rotates this direction `Y` axis clockwise.
   */
  fun rotateClockWiseY(): Direction {
    return when (this) {
      NORTH -> EAST
      EAST -> SOUTH
      SOUTH -> WEST
      WEST -> NORTH
      else -> this
    }
  }
  
  /**
   * Rotates this direction `Y` axis counterclockwise.
   */
  fun rotateCounterClockWiseY(): Direction {
    return when (this) {
      NORTH -> WEST
      EAST -> NORTH
      SOUTH -> EAST
      WEST -> SOUTH
      else -> this
    }
  }
  
  /**
   * Rotates this facing around the given [axis] counter-clockwise.
   */
  fun rotateAround(axis: Axis): Direction {
    return when (axis) {
      Axis.X -> if (this != WEST && this != EAST) rotateClockWiseX() else this
      Axis.Y -> if (this != UP && this != DOWN) rotateClockWiseY() else this
      Axis.Z -> if (this != NORTH && this != SOUTH) rotateClockWiseZ() else this
    }
  }
  
  override fun toString() = id
  
  companion object {
    @JvmField
    val horizontal = listOf(SOUTH, WEST, NORTH, EAST)
    
    //@JvmField
    val lookup by lazy {entries.associateBy { it.id }}
    
    /**
     * Gets a direction by index.
     */
    fun front(index: Int) = entries[abs(index % 6)]
    
    /**
     * Gets a horizontal direction by index.
     */
    fun horizontal(index: Int) = horizontal[abs(index % 4)]
    
    /**
     * Gets a horizontal direction by angle.
     */
    fun byAngle(angle: Double) = horizontal(floorInt(angle / 90 + 0.5) and 3)
    
    /**
     * Gets a horizontal direction by yaw.
     */
    fun byYaw(yaw: Float) = horizontal(floorInt(yaw * 4f / 360f + 0.5) and 3)
    
    /**
     * Gets a random direction.
     */
    fun random() = entries.fastRandom()
    
    /**
     * Gets a direction by id.
     */
    fun byId(id: String) = lookup[id.lowercase()] ?: NORTH
    
    /**
     * Gets a direciton by axis.
     */
    fun byAxis(axis: Axis, direction: AxisDirection): Direction {
      return entries.find { it.axis == axis && it.direction == direction } ?: NORTH
    }
    
    /**
     * Gets the nearest direction by pos.
     */
    fun nearest(pos: Pos) = nearest(pos.x, pos.y, pos.z)
    
    /**
     * Gets the nearest direction subtracting [end] from [start].
     */
    fun nearestFrom(start: Pos, end: Pos): Direction {
      return nearest(end.x - start.x, end.y - start.y, end.z - start.z)
    }
    
    /**
     * Gets the nearest direction by [x], [y], [z].
     */
    fun nearest(x: Double, y: Double, z: Double): Direction {
      return entries.maxByOrNull { (x * it.stepX) + (y * it.stepY) + (z * it.stepZ) } ?: NORTH
    }
  }
}
