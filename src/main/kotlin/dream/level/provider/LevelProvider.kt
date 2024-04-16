package dream.level.provider

import dream.level.Level
import dream.level.MoonPhase
import dream.level.biome.ChunkManager
import dream.misc.Open
import dream.utils.PI
import dream.utils.cos
import dream.utils.sin

/**
 * Represents a base provider for level.
 */
@Open
class LevelProvider {

  lateinit var level: Level

  fun registerWorld(level: Level) {
    this.level = level
    registerChunkManager()
    generateLightBrightnessTable()
  }

  /**
   * Determinates if this provider has sky.
   */
  var hasSky = true

  /**
   * Sunrise/Sunset RGBA colors
   */
  val sunColors = FloatArray(4)

  /**
   * Light to brightness conversion table.
   */
  val lightBrightnessTable = FloatArray(16)

  /**
   * Level chunk manager being used to generate chunks.
   */
  var chunkManager = registerChunkManager()

  val moonPhaseIndex get() = getMoonPhaseIndex()
  val moonPhase get() = getMoonPhase()

  /**
   * Creates the light to brightness table
   */
  fun generateLightBrightnessTable() {
    val light = 0.0f
    for (i in 0..15) {
      val f1 = 1.0f - i / 15.0f
      lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f) * (1.0f - light) + light
    }
  }

  fun registerChunkManager(): ChunkManager {
    return ChunkManager()
  }

  /**
   * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
   */
  fun calculateCelestialAngle(partialTicks: Float, time: Int = level.time): Float {
    val i = time % 24000
    var f = (i.toFloat() + partialTicks) / 24000.0f - 0.25f

    if (f < 0.0f) ++f
    if (f > 1.0f) --f

    f = 1.0f - ((cos(f * PI) + 1.0) / 2.0).toFloat()
    f += (f - f) / 3.0f

    return f
  }

  /**
   * Returns array with sunrise/sunset colors
   */
  fun calculateSunColors(angle: Float, partialTicks: Float): FloatArray? {
    val f = 0.4f
    val f1 = cos(angle * PI * 2.0f).toFloat()
    val f2 = -0.0f
    return if (f1 >= f2 - f && f1 <= f2 + f) {
      val f3 = (f1 - f2) / f * 0.5f + 0.5f
      var f4 = 1.0f - (1.0f - sin(f3 * PI).toFloat()) * 0.99f
      f4 *= f4
      sunColors[0] = f3 * 0.3f + 0.7f
      sunColors[1] = f3 * f3 * 0.7f + 0.2f
      sunColors[2] = f3 * f3 * 0.0f + 0.2f
      sunColors[3] = f4
      sunColors
    } else {
      FloatArray(0)
    }
  }

  fun getMoonPhaseIndex(time: Int = level.time): Int {
    return (time / 24000 % 8 + 8) % 8
  }

  fun getMoonPhase(time: Int = level.time): MoonPhase {
    return MoonPhase.byIndex(getMoonPhaseIndex(time))
  }

  init {
    generateLightBrightnessTable()
  }
}
