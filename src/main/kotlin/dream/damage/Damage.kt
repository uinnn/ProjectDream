package dream.damage

import dream.api.*
import dream.entity.base.*
import dream.misc.*

/**
 * Represents a damage data.
 */
@Open
interface Damage : Nameable {

  /**
   * If damage should ignore player blocking.
   */
  var isUnblockable: Boolean

  /**
   * Damages entity even though it's in creative mode.
   */
  var damageInCreative: Boolean

  /**
   * If damage should ignore entity armor protection.
   */
  var ignoreProtection: Boolean

  /**
   * If damage should ignore entity effects.
   */
  var ignoreEffects: Boolean

  /**
   * Determinates if the damage should be multiplied by the difficulty.
   */
  var difficultyScaled: Boolean
  
  /**
   * How much hunger will be satisfied when player is damaged.
   *
   * This only affects player entities.
   */
  var hunger: Float
  
  /**
   * The entity associated with this damage.
   *
   * Can be null if this damage is not associated with any entity.
   */
  val entity: Entity?

  /**
   * Damages the given [entity].
   */
  fun damage(entity: Entity, amount: Float) {
    entity.damageDirect(amount)
  }

  /**
   * Determinates if [entity] is damaged by this damage.
   */
  fun canDamage(entity: Entity, amount: Float): Boolean {
    return true
  }
}
