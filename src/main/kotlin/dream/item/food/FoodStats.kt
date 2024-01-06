package dream.item.food

import com.soywiz.kds.*
import dream.entity.player.*
import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*

/**
 * Represents a food stats for a player.
 */
open class FoodStats : CompoundStorable, Comparable<FoodStats> {

  constructor()
  constructor(tag: CompoundTag) {
    load(tag)
  }

  /**
   * The current nutrition level.
   */
  open var nutrition = 20
    set(value) {
      field = between(0, field + value, 20)
    }

  /**
   * The previous nutrition level.
   */
  open var prevNutrition = 20

  /**
   * The current saturation level.
   */
  open var saturation = 5f
    set(value) {
      field = between(0f, field + value, nutrition.toFloat())
    }

  /**
   * The current exhaustion level.
   */
  open var exhaustion = 0f
    set(value) {
      field = between(0f, field + value, 40f)
    }

  /**
   * The food timer.
   */
  open var timer = 0

  /**
   * Returns if it needs food.
   */
  open val needsFood
    get() = nutrition < 20

  /**
   * Returns if is hunger.
   */
  open val isHunger
    get() = nutrition <= 0

  /**
   * Add food stats by the given data.
   */
  open fun add(nutrition: Int, saturation: Float): FoodStats {
    this.nutrition += nutrition
    this.saturation += saturation
    return this
  }

  /**
   * Add food stats by the given [food].
   */
  open fun add(food: Food): FoodStats {
    return add(food.nutrition, food.saturation)
  }

  /**
   * Ticks these stats for [player].
   */
  open fun tick(player: Player) {
    prevNutrition = nutrition

    if (exhaustion > 4) {
      exhaustion -= 4
      when {
        saturation > 0 -> saturation--
      }
    }
  }

  /**
   * Saves these stats in [tag].
   */
  override fun save(tag: CompoundTag) {
    tag["foodLevel"] = nutrition
    tag["foodTickTimer"] = timer
    tag["foodSaturationLevel"] = saturation
    tag["foodExhaustionLevel"] = exhaustion
  }

  /**
   * Loads these stats by [tag].
   */
  override fun load(tag: CompoundTag) {
    nutrition = tag.int("foodLevel", 20)
    timer = tag.int("foodTickTimer")
    saturation = tag.float("foodSaturationLevel", 5f)
    exhaustion = tag.float("foodExhaustionLevel")
  }

  fun isSimilar(other: FoodStats): Boolean {
    return nutrition == other.nutrition &&
      saturation == other.saturation &&
      exhaustion == other.exhaustion &&
      timer == other.timer
  }

  override fun compareTo(other: FoodStats): Int {
    return nutrition.compareTo(other.nutrition)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is FoodStats) return false
    return isSimilar(other)
  }

  override fun hashCode(): Int {
    return hashCode(nutrition, saturation, exhaustion, timer)
  }

  override fun toString(): String {
    return "FoodStats(nutrition=$nutrition, saturation=$saturation, exhaustion=$exhaustion, timer=$timer)"
  }
}
