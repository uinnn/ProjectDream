package dream.enchantment

import dream.*
import dream.nbt.types.*
import dream.utils.*
import kotlin.collections.set

/**
 * A registry for enchantments.
 */
object Enchantments : Iterable<Enchantment> {

  val ID_LOOKUP = IntObjectMap<Enchantment>()
  val KEY_LOOKUP = HashMap<Key, Enchantment>()

  /**
   * Gets a enchantment by [id].
   */
  fun byId(id: Int): Enchantment? {
    return ID_LOOKUP.get(id)
  }

  /**
   * Gets a enchantment by [key].
   */
  fun byKey(key: Key): Enchantment? {
    return KEY_LOOKUP[key]
  }

  /**
   * Gets a enchantment by [tag].
   */
  fun byTag(tag: CompoundTag): Enchantment? {
    val id = tag.shortOrNull("id") ?: return null
    return byId(id.toInt())
  }

  /**
   * Gets a enchantment data by [tag].
   *
   * This will returns a pair of [Enchantment] and [Int] being the level in [tag].
   */
  fun getData(tag: CompoundTag): Pair<Enchantment, Int> {
    val ench = byTag(tag) ?: error("Tag $tag not contains any enchantment.")
    val level = tag.int("level")
    return ench to level
  }

  /**
   * Gets a enchantment data by [tag].
   *
   * This will returns a pair of [Enchantment] and [Int] being the level in [tag].
   */
  fun getDataOrNull(tag: CompoundTag): Pair<Enchantment, Int>? {
    val ench = byTag(tag) ?: return null
    val level = tag.int("level")
    return ench to level
  }

  /**
   * Register a new [enchantment].
   */
  fun <T : Enchantment> register(enchantment: T): T {
    ID_LOOKUP.put(enchantment.id, enchantment)
    KEY_LOOKUP[enchantment.key] = enchantment
    return enchantment
  }

  init {
    registerVanilla()
  }

  /**
   * Register all vanilla enchantments.
   */
  private fun registerVanilla() {
    register(Sharpness)
  }

  override fun iterator(): Iterator<Enchantment> {
    return ID_LOOKUP.values.iterator()
  }
}
