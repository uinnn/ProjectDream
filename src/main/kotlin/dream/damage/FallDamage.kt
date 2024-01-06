package dream.damage

import dream.entity.base.*

/**
 * Represents a fall damage.
 */
object FallDamage : AbstractDamage("Fall") {
  override fun canDamage(entity: Entity, amount: Float): Boolean {
    return entity.onFallDamage(amount, 0.5f)
  }
}
