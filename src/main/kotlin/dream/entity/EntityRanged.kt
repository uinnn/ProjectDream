package dream.entity

import dream.entity.base.*

/**
 * A base interface for all ranged mobs.
 */
interface EntityRanged {

  /**
   * Attacks [target] in range.
   */
  fun attackRanged(target: EntityLiving, distance: Float)
}
