package dream.attribute

/**
 * Enumeration representing different attribute operations.
 *
 * The [Operation] enum class defines the available operations: SUM, PERCENT, and MULTIPLIER.
 */
enum class Operation {
  
  /**
   * Represents the SUM operation.
   */
  SUM,
  
  /**
   * Represents the PERCENT operation.
   */
  PERCENT,
  
  /**
   * Represents the MULTIPLIER operation.
   */
  MULTIPLIER;
  
  /**
   * Returns the ID of the operation.
   */
  val id get() = ordinal
  
  companion object {
    
    /**
     * Retrieves an operation based on its ID.
     *
     * @param id The ID of the operation.
     * @return The corresponding [Operation] instance.
     */
    fun byId(id: Int) = when (id) {
      1 -> PERCENT
      2 -> MULTIPLIER
      else -> SUM
    }
  }
}
