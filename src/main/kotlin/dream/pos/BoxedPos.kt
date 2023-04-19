package dream.pos

/**
 * Represents a sequence of [Pos] in a boxed state.
 */
class BoxedPos(val min: Pos, val max: Pos) : Sequence<Pos> {
   
   override fun iterator() = object : Iterator<Pos> {
      
      // the cached last returned pos from [next]
      var lastReturned: Pos? = null
      
      override fun hasNext(): Boolean {
         return lastReturned == null || lastReturned != max
      }
      
      override fun next(): Pos {
         val last = lastReturned ?: run {
            lastReturned = min
            return min
         }
         
         val x = last.x
         val y = last.y
         val z = last.z
         
         lastReturned = when {
            x < max.x -> Pos(x + 1.0, y, z)
            y < max.y -> Pos(min.x, y + 1.0, z)
            z < max.z -> Pos(min.x, min.y, z + 1.0)
            else -> null
         }
         
         return lastReturned!!
      }
      
   }
}
