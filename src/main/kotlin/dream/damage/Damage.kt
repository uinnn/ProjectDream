package dream.damage

import dream.entity.*
import dream.interfaces.*
import dream.misc.*

/**
 * Represents a damage data.
 */
@Open
public interface Damage : Nameable {
   
   /**
    * If damage should ignore player blocking.
    */
   public var isUnblockable: Boolean
   
   /**
    * Damages entity even though it's in creative mode.
    */
   public var damageInCreative: Boolean
   
   /**
    * If damage should ignore entity armor protection.
    */
   public var ignoreProtection: Boolean
   
   /**
    * If damage should ignore entity effects.
    */
   public var ignoreEffects: Boolean
   
   /**
    * Determinates if the damage should be multiplied by the difficulty.
    */
   public var difficultyScaled: Boolean
   
   /**
    * Damages the given [entity].
    */
   public fun damage(entity: Entity, amount: Float) {
      entity.damageIgnore(amount)
   }
   
   /**
    * Determinates if [entity] is damaged by this damage.
    */
   public fun canDamage(entity: Entity, amount: Float): Boolean {
      return true
   }
}
