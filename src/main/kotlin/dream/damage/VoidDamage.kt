package dream.damage

import dream.entity.base.*

/**
 * Represents a void damage.
 */
object VoidDamage : AbstractDamage("Void") {
  init {
    isUnblockable = true
    damageInCreative = true
  }

  override fun canDamage(entity: Entity, amount: Float): Boolean {
    return entity.onVoidDamage(amount)
  }
}
