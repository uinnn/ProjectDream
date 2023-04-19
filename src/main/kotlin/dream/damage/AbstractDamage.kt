package dream.damage

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
}
