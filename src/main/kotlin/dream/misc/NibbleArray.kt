package dream.misc

/**
 * Represents a nibble array.
 *
 * Minecraft uses nibble array to store chunk data or light map data.
 */
class NibbleArray(val data: ByteArray = ByteArray(2048)) {
   init {
      checkSize()
   }
   
   /**
    * Returns the nibble of data corresponding to the passed in x, y, z. y is at most 6 bits, z is at most 4.
    */
   operator fun get(x: Int, y: Int, z: Int): Int {
      return get(getCoordinateIndex(x, y, z))
   }
   
   /**
    * Arguments are x, y, z, val. Sets the nibble of data at x << 11 | z << 7 | y to val.
    */
   operator fun set(x: Int, y: Int, z: Int, value: Int) {
      set(getCoordinateIndex(x, y, z), value)
   }
   
   /**
    * Sets [value] in [index] on this nibble array.
    */
   operator fun set(index: Int, value: Int) {
      val i = getNibbleIndex(index)
      val shift = if (isLowerNibble(index)) 0 else 4
      data[i] = (data[i].toInt() and (0xF shl shift).inv() or (value shl shift)).toByte()
   }
   
   /**
    * Gets a value at [index] on this nibble array.
    */
   fun get(index: Int): Int {
      return (data[getNibbleIndex(index)].toInt() shr if (isLowerNibble(index)) 0 else 4) and 0xF
   }
   
   /**
    * Gets a codified coordinate index for the givens x,y,z positions.
    */
   fun getCoordinateIndex(x: Int, y: Int, z: Int): Int {
      return y shl 8 or (z shl 4) or x
   }
   
   /**
    * Verifies if [index] is on lower nibble.
    */
   fun isLowerNibble(index: Int): Boolean {
      return index and 1 == 0
   }
   
   /**
    * Gets a nibble index based on specified [index].
    */
   fun getNibbleIndex(index: Int): Int {
      return index shr 1
   }
   
   /**
    * Checks the size of the data array.
    */
   private fun checkSize() {
      if (data.size != 2048) {
         error("NibbleArray data should have 2048 bytes, not ${data.size}")
      }
   }
}
