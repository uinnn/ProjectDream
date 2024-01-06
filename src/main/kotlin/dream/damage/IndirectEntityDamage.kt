package dream.damage

import dream.chat.*
import dream.entity.base.*

class IndirectEntityDamage(
  entity: Entity,
  val indirectEntity: Entity,
  name: String,
) : EntityDamage(entity, name = name) {
  
  /**
   * Generates a death message for the specified dead entity.
   *
   * @param dead The entity that was killed.
   * @return The death message component.
   */
  override fun getDeathMessage(dead: EntityLiving): Component? {
    return text("${dead.name} killed by ${indirectEntity.name}")
  }
  
}
