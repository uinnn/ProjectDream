package dream.damage

import dream.entity.*
import dream.entity.effect.*

/**
 * Represents a lightning bolt damage.
 */
class LightningBoltDamage(val bolt: LightningBolt) :
   AreaDamage("LightningBolt", bolt.radiusX, bolt.radiusY, bolt.radiusZ) {
   
   override fun areaDamage(main: Entity, entity: Entity, amount: Float) {
      entity.damageIgnore(amount)
      if (++entity.fireTicks == 0) {
         entity.fireTicks = 8
      }
   }
   
   override fun canDamage(entity: Entity, amount: Float): Boolean {
      return entity.onLightningDamage(bolt, this, amount)
   }
}
