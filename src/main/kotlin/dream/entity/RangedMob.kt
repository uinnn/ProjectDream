package dream.entity

/**
 * A base interface for all ranged mobs.
 */
interface RangedMob {
   
   /**
    * Attacks [target] in range.
    */
   fun attackRanged(target: EntityLiving, distance: Float)
}
