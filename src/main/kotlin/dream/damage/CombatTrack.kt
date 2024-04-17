package dream.damage

import dream.chat.*
import dream.entity.base.*
import dream.entity.player.*
import dream.misc.*

/**
 * A class representing the combat track of an entity.
 *
 * @param entity The living entity being tracked.
 */
@Open
class CombatTrack(val entity: EntityLiving) : Iterable<CombatEntry> {
  
  /**
   * List of combat entries.
   */
  val entries = ArrayList<CombatEntry>()
  
  /**
   * The time of the last damage taken.
   */
  var lastDamageTime = 0
  
  /**
   * The start time of the combat.
   */
  var combatStartTime = 0
  
  /**
   * The end time of the combat.
   */
  var combatEndTime = 0
  
  /**
   * Flag indicating whether the entity is currently in combat.
   */
  var isOnCombat = false
    get() {
      recheckStatus()
      return field
    }
  
  /**
   * Flag indicating whether the entity is currently taking damage.
   */
  var isTakingDamage = false
    get() {
      recheckStatus()
      return field
    }
  
  /**
   * The next location of the entity.
   */
  var nextLocation = ""
  
  /**
   * Gets the last combat entry in the track, or null if there are no entries.
   */
  val lastEntry: CombatEntry?
    get() = entries.lastOrNull()
  
  /**
   * Gets the duration of the current combat session in ticks.
   */
  val combatDuration: Int
    get() = if (isOnCombat) entity.ticks - combatStartTime else combatEndTime - combatStartTime
  
  /**
   * Creates a new combat entry for the given damage, amount, and health values.
   * @param damage The type of damage.
   * @param amount The amount of damage.
   * @param health The health of the entity after taking the damage.
   * @return The created combat entry.
   */
  fun createEntry(damage: Damage, amount: Float, health: Float): CombatEntry {
    return CombatEntry(damage, entity.ticks, amount, health, entity.fallDistance, nextLocation)
  }
  
  /**
   * Tracks the given damage, amount, and health values in the combat track.
   * @param damage The type of damage.
   * @param amount The amount of damage.
   * @param health The health of the entity after taking the damage.
   */
  fun track(damage: Damage, amount: Float, health: Float) {
    recheckStatus()
    prepareLocation()
    val entry = createEntry(damage, amount, health)
    entries += entry
    lastDamageTime = entity.ticks
    isTakingDamage = true
    if (entry.isCombatRelated && !isOnCombat && entity.isAlive) {
      isOnCombat = true
      combatStartTime = entity.ticks
      combatEndTime = combatStartTime
      entity.onEnterCombat()
    }
  }
  
  /**
   * Prepares the next location value based on the entity's current state.
   */
  fun prepareLocation() {
    resetLocation()
    
    nextLocation = when {
      entity.isOnWater -> "water"
      else -> {
        val state = entity.stateInside
        when {
          //state.its(Blocks.LADDER) -> "ladder"
          //state.its(Blocks.VINES) -> "ladder"
          else -> ""
        }
      }
    }
  }
  
  /**
   * Resets the next location value to an empty string.
   */
  fun resetLocation() {
    nextLocation = ""
  }
  
  /**
   * Rechecks the combat status of the entity and updates the relevant properties.
   */
  fun recheckStatus() {
    val time = if (isOnCombat) 300 else 100
    if (isTakingDamage && (!entity.isAlive || entity.ticks - lastDamageTime > time)) {
      val haveBeenInCombat = isOnCombat
      isTakingDamage = false
      isOnCombat = false
      combatEndTime = entity.ticks
      if (haveBeenInCombat) {
        entity.onExitCombat()
      }
      
      entries.clear()
    }
  }
  
  /**
   * Searches on [entries] for the killer of this combat.
   */
  fun searchKiller(): EntityLiving? {
    var killerEntity: EntityLiving? = null
    var killerPlayer: Player? = null
    var damageEntity = 0f
    var damagePlayer = 0f
    
    for (entry in entries) {
      val damage = entry.damage
      val amount = entry.damageAmount
      val entity = damage.entity
      
      if (entity is Player && (killerPlayer == null || amount > damagePlayer)) {
        damagePlayer = amount
        killerPlayer = entity
      }
      
      if (entity is EntityLiving && (killerEntity == null || amount > damageEntity)) {
        damageEntity = amount
        killerEntity = entity
      }
    }
    
    return if (killerPlayer != null && damagePlayer >= damageEntity / 3) killerPlayer else killerEntity
  }
  
  /**
   * Gets the death message for the entity.
   * @return The death message component.
   */
  fun getDeathMessage(): Component {
    return text("Entity killeds another entity.")
  }
  
  override fun iterator(): Iterator<CombatEntry> {
    return entries.iterator()
  }
}
