package dream.entity

import dream.entity.base.Entity

/**
 * Represents a complex entity.
 *
 * Complex entities can have various parts [EntityPart].
 */
interface EntityComplex {

  /**
   * Gets the complex owner entity.
   */
  val owner: Entity

  /**
   * Gets all parts.
   */
  val parts: List<EntityPart>

  /**
   * Called when a part of this entity is attacked.
   */
  fun attackByPart(part: EntityPart)
}
