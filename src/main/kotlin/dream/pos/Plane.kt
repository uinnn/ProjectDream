package dream.pos

import com.soywiz.kds.random.*
import dream.utils.*

/**
 * Represents all surface plane type.
 */
public enum class Plane(val directions: List<Direction>) : Predicate<Direction>, Iterable<Direction> {
   VERTICAL(Direction.UP, Direction.DOWN),
   HORIZONTAL(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
   
   constructor(vararg directions: Direction) : this(directions.toList())
   
   /**
    * Returns a random direction from this plane.
    */
   fun random() = directions.fastRandom()
   
   override fun invoke(t: Direction) = t.axis.plane == this
   override fun iterator() = directions.iterator()
}

