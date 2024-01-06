package dream.damage

import dream.entity.base.*
import dream.misc.*

/**
 * Represents a combat entry, containing information about a specific damage event.
 *
 * @param damage The EntityDamage instance associated with the combat entry.
 * @param time The time of the combat event.
 * @param damageAmount The amount of damage inflicted.
 * @param health The health of the entity after the damage.
 * @param fallDistance The fall distance of the entity.
 * @param location The location of the combat event. Default value is an empty string.
 */
@Open
data class CombatEntry(
  val damage: Damage,
  val time: Int,
  val damageAmount: Float,
  val health: Float,
  val fallDistance: Float,
  val location: String = ""
) {
  
  /**
   * Gets the health of the entity after the damage.
   */
  val healthAfterDamage get() = health - damageAmount
  
  /**
   * Checks if the combat entry is related to combat with a living entity.
   */
  val isCombatRelated get() = damage.entity != null && damage.entity is EntityLiving
  
  /**
   * Gets the attacker entity.
   */
  val attacker get() = damage.entity
  
  /**
   * Gets the name of the attacker entity.
   */
  val attackerName get() = attacker?.displayName

}
