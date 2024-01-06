package dream.entity

/**
 * Represents a entity part of a complex entity.
 */
interface EntityPart {

  /**
   * Gets the parent owner of this part.
   */
  val parent: EntityComplex

  /**
   * The name of this part.
   */
  val partName: String
}
