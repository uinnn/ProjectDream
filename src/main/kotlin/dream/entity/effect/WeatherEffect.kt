package dream.entity.effect

import dream.entity.*
import dream.level.*

/**
 * Represents a weather effect entity.
 */
abstract class WeatherEffect : Entity {
   
   constructor()
   constructor(level: Level) : super(level)
   constructor(x: Double, y: Double, z: Double) : super(x, y, z)
   constructor(level: Level, x: Double, y: Double, z: Double) : super(level, x, y, z)
   
}
