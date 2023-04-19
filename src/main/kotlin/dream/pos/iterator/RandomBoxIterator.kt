package dream.pos.iterator

import com.soywiz.kds.random.*
import dream.pos.*

/**
 * A random box iterator.
 *
 * Iterates in every pos between min pos and max pos randomly.
 */
open class RandomBoxIterator(
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
   
   var rangeX = 1..1
   var rangeY = 1..1
   var rangeZ = 1..1
   
   fun clamped(): RandomBoxIterator {
      rangeX = minX..maxX
      rangeY = minY..maxY
      rangeZ = minZ..maxZ
      return this
   }
   
   fun clampedX(): RandomBoxIterator {
      rangeX = minX..maxX
      return this
   }
   
   fun clampedY(): RandomBoxIterator {
      rangeY = minY..maxY
      return this
   }
   
   fun clampedZ(): RandomBoxIterator {
      rangeZ = minZ..maxZ
      return this
   }
   
   infix fun range(range: IntRange): RandomBoxIterator {
      rangeX = range
      rangeY = range
      rangeZ = range
      return this
   }
   
   infix fun rangeX(range: IntRange): RandomBoxIterator {
      rangeX = range
      return this
   }
   
   infix fun rangeY(range: IntRange): RandomBoxIterator {
      rangeY = range
      return this
   }
   
   infix fun rangeZ(range: IntRange): RandomBoxIterator {
      rangeZ = range
      return this
   }
   
   override fun nextX(): Boolean {
      x += rangeX.random(FastRandom)
      return x >= maxX
   }
   
   override fun nextY(): Boolean {
      y += rangeY.random(FastRandom)
      return y >= maxY
   }
   
   override fun nextZ(): Boolean {
      z += rangeZ.random(FastRandom)
      return z >= maxZ
   }
}
