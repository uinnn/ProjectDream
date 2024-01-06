package dream.village

import dream.item.*
import dream.nbt.*
import dream.nbt.types.*
import dream.utils.*

/**
 * A list of all recipes of a merchant.
 */
class MerchantTradeList : ArrayList<MerchantTrade>, CompoundStorable {

  constructor()
  constructor(tag: CompoundTag) {
    load(tag)
  }

  /**
   * Gets all enabled recipes
   */
  val enabledRecipes get() = filter { !it.isDisabled() }

  /**
   * Gets all disabled recipes
   */
  val disabledRecipes get() = filter { it.isDisabled() }

  /**
   * Finds a tradeable recipe on this list based on the given items.
   */
  fun findTradeableRecipe(first: ItemStack, second: ItemStack = EmptyItemStack): MerchantTrade? {
    return find { it.canTrade(first, second) }
  }

  /**
   * Gets a recipe at the given [index] and returns them if is tradeable based on the given items.
   */
  fun findTradeableRecipe(index: Int, first: ItemStack, second: ItemStack = EmptyItemStack): MerchantTrade? {
    return getOrNull(index)?.takeIf { it.canTrade(first, second) }
  }

  override fun save(tag: CompoundTag) {
    tag["Recipes"] = elementsToTag()
  }

  override fun load(tag: CompoundTag) {
    tag.compoundListOrNull("Recipes")?.forEach { recipe -> add(MerchantTrade(recipe)) }
  }

}
