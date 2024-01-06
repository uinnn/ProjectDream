package dream.level

import dream.misc.*
import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*
import kotlinx.serialization.*
import java.util.*

/**
 * Represents a storage for world game rules.
 */
@Open
class GameRules : TreeMap<String, GameRule>, CompoundStorable {

  constructor()
  constructor(tag: CompoundTag) {
    load(tag)
  }

  init {
    defaults()
  }

  /**
   * Gets all rules.
   */
  val rules get() = values

  /**
   * Sets the defaults game rules.
   */
  fun defaults() {
    clear()
    add("doFireTick", "true", GameRuleType.BOOLEAN)
    add("mobGriefing", "true", GameRuleType.BOOLEAN)
    add("keepInventory", "false", GameRuleType.BOOLEAN)
    add("doMobSpawning", "true", GameRuleType.BOOLEAN)
    add("doMobLoot", "true", GameRuleType.BOOLEAN)
    add("doTileDrops", "true", GameRuleType.BOOLEAN)
    add("doEntityDrops", "true", GameRuleType.BOOLEAN)
    add("commandBlockOutput", "true", GameRuleType.BOOLEAN)
    add("naturalRegeneration", "true", GameRuleType.BOOLEAN)
    add("doDaylightCycle", "true", GameRuleType.BOOLEAN)
    add("logAdminCommands", "true", GameRuleType.BOOLEAN)
    add("showDeathMessages", "true", GameRuleType.BOOLEAN)
    add("randomTickSpeed", "3", GameRuleType.NUMERICAL)
    add("sendCommandFeedback", "true", GameRuleType.BOOLEAN)
    add("reducedDebugInfo", "false", GameRuleType.BOOLEAN)
  }

  /**
   * Adds a new [GameRule] on this rules by the given data.
   */
  fun add(key: String, value: String, type: GameRuleType): GameRules {
    put(key, GameRule(type, value))
    return this
  }

  /**
   * Updates an existent [GameRule] by [value] or creates a new one.
   */
  fun setOrAdd(key: String, value: String): GameRules {
    val rule = get(key)

    if (rule == null) {
      add(key, value, GameRuleType.ANY)
    } else {
      rule.value = value
    }

    return this
  }

  /**
   * Gets a string game rule.
   */
  fun string(key: String): String {
    val rule = get(key) ?: return ""
    return rule.value
  }

  /**
   * Gets a boolean game rule.
   */
  fun boolean(key: String): Boolean {
    val rule = get(key) ?: return false
    return rule.toBoolean()
  }

  /**
   * Gets an int game rule.
   */
  fun int(key: String): Int {
    val rule = get(key) ?: return 0
    return rule.toInt()
  }

  /**
   * Gets a double game rule.
   */
  fun double(key: String): Double {
    val rule = get(key) ?: return 0.0
    return rule.toDouble()
  }

  /**
   * Saves this rules in [tag].
   */
  override fun save(tag: CompoundTag) {
    for ((key, value) in this) {
      tag[key] = value.value
    }
  }

  /**
   * Loads this rules by [tag].
   */
  override fun load(tag: CompoundTag) {
    for (key in tag.keys) {
      setOrAdd(key, tag.string(key))
    }
  }
}

/**
 * Represents a game rule entry.
 */
@Serializable
data class GameRule(val type: GameRuleType, var value: String) {

  /**
   * Converts this rule value to boolean.
   */
  fun toBoolean() = value.toBoolean()

  /**
   * Converts this rule value to int.
   */
  fun toInt() = value.toIntOrDefault()

  /**
   * Converts this rule value to double.
   */
  fun toDouble() = value.toDoubleOrDefault()
}

/**
 * Represents all value type of game rule entry.
 */
enum class GameRuleType {
  ANY,
  BOOLEAN,
  NUMERICAL;

  /**
   * Returns if this game rule type is a boolean type.
   */
  val isBoolean: Boolean
    get() = this == ANY || this == BOOLEAN

  /**
   * Returns if this game rule type is a numerical type.
   */
  val isNumerical: Boolean
    get() = this == ANY || this == NUMERICAL
}
