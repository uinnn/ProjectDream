package dream.entity.effect

import dream.damage.LightningBoltDamage
import dream.level.Level

/**
 * Represents a lightning bolt entity weather effect.
 */
class LightningBolt : WeatherEffect {

  /**
   * `X` radius for area damage
   */
  var radiusX = 3.0

  /**
   * `Y` radius for area damage
   */
  var radiusY = 3.0

  /**
   * `Z` radius for area damage
   */
  var radiusZ = 3.0

  /**
   * The damage caused when striked.
   */
  var damage = 5f

  constructor()
  constructor(level: Level) : super(level)
  constructor(x: Double, y: Double, z: Double) : super(x, y, z)
  constructor(level: Level, x: Double, y: Double, z: Double) : super(level, x, y, z)

  /**
   * Creates a [LightningBoltDamage] by this lightning bolt entity.
   */
  fun createDamage(): LightningBoltDamage {
    return LightningBoltDamage(this)
  }
}
