package dream.entity.boss

import dream.api.Nameable

/**
 * Represents data to be displayed on boss bar.
 */
interface BossDisplayData : Nameable {

  /**
   * The current health of the boss.
   */
  val health: Float

  /**
   * The max health of the boss.
   */
  val maxHealth: Float
}
