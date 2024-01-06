package dream.entity.boss

import dream.entity.EntityRanged
import dream.entity.base.*

class Wither(override val health: Float, override val maxHealth: Float) : Mob(), BossDisplayData, EntityRanged {
  override fun attackRanged(target: EntityLiving, distance: Float) {
    TODO("Not yet implemented")
  }
}
