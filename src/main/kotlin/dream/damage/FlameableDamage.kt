package dream.damage

import dream.entity.*

/**
 * Represents a damage associated with fire.
 */
abstract class FlameableDamage(name: String) : PhysicalDamage(name) {
   override fun canDamage(entity: Entity, amount: Float): Boolean {
      return !entity.isFireImmune
   }
}

/**
 * Returns if this damage is a flameable damage.
 */
val Damage.isFlameable: Boolean
   get() = this is FlameableDamage
