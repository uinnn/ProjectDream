package dream.entity.boss

/**
 * Status for current boss.
 */
object BossStatus {

  /**
   * Gets the current health scale of the boss status.
   */
  var healthScale = 0f

  /**
   * Gets the current status bar time of the boss status.
   */
  var statusBarTime = 0

  /**
   * Gets the current boss name of the boss status.
   */
  var bossName = ""

  /**
   * Determinates if the boss bar has color modifier.
   */
  var hasColorModifier = false

  /**
   * Sets the current status by [data].
   */
  fun setStatus(data: BossDisplayData, hasColor: Boolean = false) {
    healthScale = data.health / data.maxHealth
    statusBarTime = 100
    bossName = data.name
    hasColorModifier = hasColor
  }
}
