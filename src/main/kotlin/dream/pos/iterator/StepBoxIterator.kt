package dream.pos.iterator

import dream.pos.*

/**
 * A box iterator.
 *
 * Iterates in every pos between min pos and max pos with a step.
 */
open class StepBoxIterator(
   minX: Int,
   minY: Int,
   minZ: Int,
   maxX: Int,
   maxY: Int,
   maxZ: Int,
) : BoxIterator(minX, minY, minZ, maxX, maxY, maxZ) {
   
   constructor(min: Pos, max: Pos) : this(
      min.flooredX,
      min.flooredY,
      min.flooredZ,
      max.flooredX,
      max.flooredY,
      max.flooredZ
   )
   
   var stepX = 1
   var stepY = 1
   var stepZ = 1
   
   infix fun step(n: Int): StepBoxIterator {
      stepX = n
      stepY = n
      stepZ = n
      return this
   }
   
   infix fun stepX(x: Int): StepBoxIterator {
      stepX = x
      return this
   }
   
   infix fun stepY(y: Int): StepBoxIterator {
      stepX = y
      return this
   }
   
   infix fun stepZ(z: Int): StepBoxIterator {
      stepX = z
      return this
   }
   
   override fun nextX(): Boolean {
      x += stepX
      return x >= maxX
   }
   
   override fun nextY(): Boolean {
      y += stepY
      return y >= maxY
   }
   
   override fun nextZ(): Boolean {
      z += stepZ
      return z >= maxZ
   }
}
