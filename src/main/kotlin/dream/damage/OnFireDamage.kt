package dream.damage

import dream.entity.base.Entity

/**
 * Represents a flameable damage when entity is on fire ``(effect)``.
 */
object OnFireDamage : FlameableDamage("OnFire") {
  override fun damage(entity: Entity, amount: Float) {
    if (entity.onFireDamage(amount)) {
      entity.damageDirect(amount)
    }
  }
}
