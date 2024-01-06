package dream.damage

import dream.entity.base.*

/**
 * Represents a base damage implementation.
 */
abstract class AbstractDamage(override val name: String) : Damage {

  /**
   * If damage should ignore player blocking.
   */
  override var isUnblockable = false

  /**
   * Damages entity even though it's in creative mode.
   */
  override var damageInCreative = false

  /**
   * If damage should ignore entity armor protection.
   */
  override var ignoreProtection = false

  /**
   * If damage should ignore entity effects.
   */
  override var ignoreEffects = false

  /**
   * Determinates if the damage should be multiplied by the difficulty.
   */
  override var difficultyScaled = false
  
  /**
   * How much hunger will be satisfied when player is damaged.
   *
   * This only affects player entities.
   */
  override var hunger: Float = 0f
  
  /**
   * The entity associated with this damage.
   *
   * Can be null if this damage is not associated with any entity.
   */
  override val entity: Entity?
    get() = null
  
}
