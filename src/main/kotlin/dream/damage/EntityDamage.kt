package dream.damage

import dream.chat.*
import dream.entity.base.*
import dream.entity.player.*

/**
 * Class representing damage inflicted by an entity.
 *
 * @param entity The entity causing the damage.
 * @param isThorns Indicates if the damage is caused by thorns.
 */
class EntityDamage(
  override val entity: Entity,
  var isThorns: Boolean = false,
  name: String = "Entity"
) : AbstractDamage(name) {
  
  /**
   * Checks if the damage is inflicted on a living entity.
   */
  val isLivingEntity get() = entity is EntityLiving
  
  /**
   * Checks if the damage is inflicted on a player.
   */
  val isPlayer get() = entity is Player
  
  /**
   * Checks if the damage is scaled according to the game difficulty.
   *
   * @return `true` if the damage is scaled, `false` otherwise.
   */
  override var difficultyScaled = isLivingEntity && !isPlayer
  
  /**
   * Generates a death message for the specified dead entity.
   *
   * @param dead The entity that was killed.
   * @return The death message component.
   */
  fun getDeathMessage(dead: EntityLiving): Component? {
    return text("${dead.name} killed by ${entity.name}")
  }
}

/**
 * Gets if this damage is caused by an entity.
 */
val Damage.isCausedByEntity get() = entity != null
