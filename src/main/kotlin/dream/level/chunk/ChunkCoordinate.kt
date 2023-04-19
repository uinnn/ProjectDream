package dream.level.chunk

import dream.pos.*

/**
 * Represents a chunk coordinate pair.
 */
public data class ChunkCoordinate(val x: Int, val z: Int) {
   
   /**
    * The center X of this chunk.
    */
   public val centerX get() = (x shl 4) + 8
   
   /**
    * The center Z of this chunk.
    */
   public val centerZ get() = (z shl 4) + 8
   
   /**
    * Get the first world X coordinate that belongs to this Chunk.
    */
   public val startX get() = x shl 4
   
   /**
    * Get the first world Z coordinate that belongs to this Chunk.
    */
   public val startZ get() = z shl 4
   
   /**
    * Get the last world X coordinate that belongs to this Chunk.
    */
   public val endX get() = (x shl 4) + 15
   
   /**
    * Get the last world Z coordinate that belongs to this Chunk.
    */
   public val endZ get() = (z shl 4) + 15
   
   /**
    * Get the World coordinates of the Block with the given Chunk coordinates relative to this chunk.
    */
   public fun getPos(x: Int, y: Int, z: Int): Pos {
      return Pos(startX + x, y, startZ + z)
   }
   
   /**
    * Get the coordinates of the Block in the center of this chunk with the given Y coordinate.
    */
   fun getCenterPos(y: Int): Pos {
      return Pos(centerX, y, centerZ)
   }
   
   /**
    * Converts this chunk coordinates to long.
    */
   public fun toLong(): Long {
      return chunkCoordinateToLong(x, z)
   }
   
   override fun toString(): String {
      return "[$x, $z]"
   }
   
   companion object {
      
      /**
       * Converts the chunk coordinates to long.
       */
      fun chunkCoordinateToLong(x: Int, z: Int): Long {
         return x.toLong() and 4294967295L or (z.toLong() and 4294967295L shl 32)
      }
   }
}
