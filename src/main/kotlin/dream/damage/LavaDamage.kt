package dream.damage

import dream.entity.base.Entity

/**
 * Represents a flameable damage when entity is in lava.
 */
object LavaDamage : FlameableDamage("Lava") {
  override fun damage(entity: Entity, amount: Float) {
    entity.fireTicks = 15
    super.damage(entity, amount)
  }

  override fun canDamage(entity: Entity, amount: Float): Boolean {
    return super.canDamage(entity, amount) && entity.onLavaDamage(amount)
  }
}
