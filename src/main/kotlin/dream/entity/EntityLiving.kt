package dream.entity

import dream.damage.*
import dream.level.*

/**
 * A living entity in Minecraft.
 *
 * Living entities support a more complex type of entities.
 */
abstract class EntityLiving : Entity {
   
   constructor()
   constructor(level: Level) : super(level)
   constructor(x: Double, y: Double, z: Double) : super(x, y, z)
   constructor(level: Level, x: Double, y: Double, z: Double) : super(level, x, y, z)
   
   override fun isInvulnerable(damage: Damage, amount: Float): Boolean {
      return super.isInvulnerable(damage, amount) && !damage.canDamage(this, amount)
   }
}
