package dream.entity.boss

import dream.entity.*

/**
 * Represents a part of a dragon.
 */
class DragonPart(
   override val parent: EntityComplex,
   override val partName: String,
   width: Float,
   height: Float,
) : Entity(parent.owner.level), EntityPart {
   
   init {
      setSize(width, height)
   }
   
   override fun canCollide(): Boolean {
      return false
   }
   
}
