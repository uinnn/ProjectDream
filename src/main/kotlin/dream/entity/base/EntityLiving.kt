package dream.entity.base

import dream.damage.*
import dream.item.*
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
  
  val heldItem: ItemStack = EmptyItemStack
  
  /**
   * The combat track for the entity.
   */
  val combatTrack = createCombatTrack()
  
  /**
   * List of combat entries.
   */
  val combatEntries get() = combatTrack.entries
  
  /**
   * Gets the duration of the current combat session in ticks.
   */
  val combatDuration get() = combatTrack.combatDuration
  
  /**
   * The time of the last damage taken.
   */
  var lastDamageTime: Int
    get() = combatTrack.lastDamageTime
    set(value) {
      combatTrack.lastDamageTime = value
    }
  
  /**
   * Flag indicating whether the entity is currently taking damage.
   */
  var isTakingDamage: Boolean
    get() = combatTrack.isTakingDamage
    set(value) {
      combatTrack.isTakingDamage = value
    }
  
  /**
   * Flag indicating whether the entity is currently in combat.
   */
  var isOnCombat: Boolean
    get() = combatTrack.isOnCombat
    set(value) {
      combatTrack.isOnCombat = value
    }
  
  /**
   * Creates a combat track for this entity.
   */
  fun createCombatTrack(): CombatTrack {
    return CombatTrack(this)
  }
  
  /**
   * Called when the entity enters combat.
   */
  fun onEnterCombat() {
  }
  
  /**
   * Called when the entity exits combat.
   */
  fun onExitCombat() {
  }
  
  override fun isInvulnerable(damage: Damage, amount: Float): Boolean {
    return super.isInvulnerable(damage, amount) && !damage.canDamage(this, amount)
  }
}
