package dream.damage

import dream.entity.*

/**
 * Represents a flameable damage when entity is in fire ``(block)``.
 */
object InFireDamage : FlameableDamage("InFire") {
   override fun damage(entity: Entity, amount: Float) {
      if (entity.onInFireDamage(amount)) {
         entity.damageIgnore(amount)
      }
   }
}
