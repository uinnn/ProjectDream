package dream.block

import dream.*
import dream.entity.player.*
import dream.misc.*

/**
 * Represents sound data of a block.
 */
@Open
data class BlockSound(
  var digSound: Sound,
  var stepSound: Sound,
  var placeSound: Sound = digSound,
  var volume: Float = 1f,
  var pitch: Float = 1f,
) {

  /**
   * Plays the dig/break sound for [player].
   */
  fun playDig(player: Player) = digSound.play(player, volume, pitch)

  /**
   * Plays the step sound for [player].
   */
  fun playStep(player: Player) = stepSound.play(player, volume, pitch)

  /**
   * Plays the place sound for [player].
   */
  fun playPlace(player: Player) = placeSound.play(player, volume, pitch)

  companion object {

    @JvmField
    val STONE = BlockSound(Sound.DIG_STONE, Sound.STEP_STONE)
    @JvmField
    val WOOD = BlockSound(Sound.DIG_WOOD, Sound.STEP_WOOD)
    @JvmField
    val GRAVEL = BlockSound(Sound.DIG_GRAVEL, Sound.STEP_GRAVEL)
    @JvmField
    val GRASS = BlockSound(Sound.DIG_GRASS, Sound.STEP_GRASS)
    @JvmField
    val METAL = BlockSound(Sound.DIG_STONE, Sound.STEP_STONE, pitch = 1.5f)
    @JvmField
    val GLASS = BlockSound(Sound.DIG_GLASS, Sound.STEP_STONE, Sound.DIG_STONE)
    @JvmField
    val WOOL = BlockSound(Sound.DIG_WOOL, Sound.STEP_WOOL)
    @JvmField
    val SAND = BlockSound(Sound.DIG_SAND, Sound.STEP_SAND)
    @JvmField
    val SNOW = BlockSound(Sound.DIG_SNOW, Sound.STEP_SNOW)
    @JvmField
    val LADDER = BlockSound(Sound.DIG_WOOD, Sound.STEP_LADDER)
    @JvmField
    val ANVIL = BlockSound(Sound.ANVIL_BREAK, Sound.STEP_STONE, Sound.ANVIL_LAND)
    @JvmField
    val SLIME = BlockSound(Sound.SLIME_STEP_BIG, Sound.SLIME_STEP_SMALL)

  }
}
