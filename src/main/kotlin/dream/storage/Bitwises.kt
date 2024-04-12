package dream.storage

// from korlibs

/**
 * Generates a bitmask for the specified number of bits.
 * @return The bitmask.
 */
fun Int.mask(): Int = (1 shl this) - 1

/**
 * Generates a bitmask for the specified number of bits.
 * @return The bitmask.
 */
fun Long.mask(): Long = (1L shl this.toInt()) - 1L

/**
 * Inserts a value into the integer at the specified bit positions.
 * @param value The value to insert.
 * @param offset The starting bit position.
 * @param count The number of bits to insert.
 * @return The result after insertion.
 */
fun Int.insert(value: Int, offset: Int, count: Int): Int {
  val mask = count.mask()
  val clearValue = this and (mask shl offset).inv()
  return clearValue or ((value and mask) shl offset)
}

/**
 * Inserts a value into the long at the specified bit positions.
 * @param value The value to insert.
 * @param offset The starting bit position.
 * @param count The number of bits to insert.
 * @return The result after insertion.
 */
fun Long.insert(value: Long, offset: Int, count: Long): Long {
  val mask = count.mask()
  val clearValue = this and (mask shl offset).inv()
  return clearValue or ((value and mask) shl offset)
}

