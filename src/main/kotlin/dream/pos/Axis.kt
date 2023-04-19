package dream.pos

import dream.utils.*

/**
 * Represents a coordinate axis.
 */
enum class Axis(val id: String, val plane: Plane) : Predicate<Direction> {
   X("x", Plane.HORIZONTAL),
   Y("y", Plane.VERTICAL),
   Z("z", Plane.HORIZONTAL);
   
   /**
    * Returns if this axis plane is in vertical direction.
    */
   val isVertical: Boolean get() = plane == Plane.VERTICAL
   
   /**
    * Returns if this axis plane is in horizontal direction.
    */
   val isHorizontal: Boolean get() = plane == Plane.HORIZONTAL
   
   override fun invoke(t: Direction) = t.axis == this
   override fun toString() = id
   
   companion object {
      
      /**
       * Gets an axis by id.
       */
      fun byId(id: String): Axis {
         return when (id.lowercase()) {
            "y" -> Y
            "z" -> Z
            else -> X
         }
      }
   }
}

/**
 * Represents a direction for [Axis].
 */
enum class AxisDirection(val offset: Int, val description: String) {
   POSITIVE(1, "Towards positive"),
   NEGATIVE(-1, "Towards negative");
   
   override fun toString() = description
}
