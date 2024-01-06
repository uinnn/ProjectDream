package dream.item.food

import dream.effect.Effect
import dream.entity.player.Player
import dream.misc.Open

typealias FoodEffect = Pair<Effect, Float>

/**
 * Represents food data.
 */
@Open
data class Food(
  var nutrition: Int,
  var saturation: Float = 0f,
  var isMeat: Boolean = false,
  var canAlwaysEat: Boolean = false,
  var eatDuration: Int = 32,
  var effects: MutableList<FoodEffect> = ArrayList(),
) : Comparable<Food> {

  /**
   * Adds all stats of this food to [stats].
   */
  fun eat(stats: FoodStats) {
    stats.add(this)
  }

  /**
   * Applies all [effects] of this food on [player].
   */
  fun bidEffects(player: Player) {

  }

  /**
   * Sets the new nutrition value.
   */
  fun nutrition(value: Int): Food {
    nutrition = value
    return this
  }

  /**
   * Sets the new saturation value.
   */
  fun saturation(value: Float): Food {
    saturation = value
    return this
  }

  /**
   * Sets this food as meat.
   */
  fun meat(): Food {
    isMeat = true
    return this
  }

  /**
   * Sets this food to always available to eat.
   */
  fun alwaysEat(): Food {
    canAlwaysEat = true
    return this
  }

  /**
   * Sets the new eat duration value.
   */
  fun duration(duration: Int): Food {
    eatDuration = duration
    return this
  }

  /**
   * Adds a new effect to this food.
   */
  fun effect(effect: FoodEffect): Food {
    effects += effect
    return this
  }

  override fun compareTo(other: Food): Int {
    return nutrition.compareTo(other.nutrition)
  }
}
