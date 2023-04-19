package dream.pos.iterator

import com.google.common.collect.*
import dream.pos.*

/**
 * A box iterator.
 *
 * Iterates in every pos between min pos and max pos.
 */
open class BoxIterator(
   val minX: Int,
   val minY: Int,
   val minZ: Int,
   val maxX: Int,
   val maxY: Int,
   val maxZ: Int,
) : AbstractIterator<Pos>() {
   
   constructor(min: Pos, max: Pos) : this(
      min.flooredX,
      min.flooredY,
      min.flooredZ,
      max.flooredX,
      max.flooredY,
      max.flooredZ
   )
   
   /**
    * The cursor position responsable to set's the new current pos.
    */
   val cursor = MutablePos()
   var x = 0
   var y = 0
   var z = 0
   
   /**
    * Returns if it has a next position to iterate.
    */
   val hasNext: Boolean get() = x <= maxX && y <= maxY && z <= maxZ
   
   /**
    * Computes next position.
    */
   override fun computeNext(): Pos? {
      return if (!hasNext) {
         endOfData()
      } else {
         if (nextX()) {
            x = 0
            if (nextY()) {
               y = 0
               if (nextZ()) {
                  z = 0
               }
            }
         }
         
         return cursor.move(x, y, z)
      }
   }
   
   /**
    * Go to next ``X`` position
    *
    * @return if ``X`` has reached to the max.
    */
   open fun nextX(): Boolean {
      x++
      return x >= maxX
   }
   
   /**
    * Go to next ``Y`` position
    *
    * @return if ``Y`` has reached to the max.
    */
   open fun nextY(): Boolean {
      y++
      return y >= maxY
   }
   
   /**
    * Go to next ``Z`` position
    *
    * @return if ``Z`` has reached to the max.
    */
   open fun nextZ(): Boolean {
      z++
      return z >= maxZ
   }
}

