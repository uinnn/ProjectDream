package dream.village

import dream.item.EmptyItemStack
import dream.item.ItemStack
import dream.misc.Open
import dream.nbt.CompoundStorable
import dream.nbt.types.CompoundTag

/**
 * Represents an trade recipe of an merchant.
 */
@Open
class MerchantTrade(
  var buyItem: ItemStack,
  var sellItem: ItemStack,
  var secondBuyItem: ItemStack = EmptyItemStack,
  var maxTrades: Int = 7,
  var trades: Int = 0,
  var rewardsExp: Boolean = true,
) : CompoundStorable {

  constructor(tag: CompoundTag) : this(
    tag.item("Buy"),
    tag.item("Sell"),
    tag.item("SecondBuy"),
    tag.int("MaxTrades", 7),
    tag.int("Trades"),
    tag.boolean("RewardsExp", true)
  )

  /**
   * Checks if this recipe has a second buy item
   */
  val hasSecondBuyItem get() = !secondBuyItem.isAir

  /**
   * Checks if the given items can be used to trade on this recipe.
   */
  fun canTrade(first: ItemStack, second: ItemStack = EmptyItemStack): Boolean {
    return first.isSimilarAtLeast(buyItem) && (!hasSecondBuyItem || second.isSimilarAtLeast(secondBuyItem))
  }

  /**
   * Checks if this recipe is disabled.
   */
  fun isDisabled(): Boolean {
    return trades >= maxTrades
  }

  /**
   * Reset this merchant trades stats.
   */
  fun resetTrades() {
    trades = 0
  }

  /**
   * Compensates the current amount of [trades] with [maxTrades].
   */
  fun disable() {
    trades = maxTrades
  }

  /**
   * Called after a player successfully trades this trade.
   */
  fun onTrade(merchant: Merchant) {

  }

  override fun save(tag: CompoundTag) {
    tag["Buy"] = buyItem.store()
    tag["Sell"] = sellItem.store()
    tag["MaxTrades"] = maxTrades
    tag["Trades"] = trades
    tag["RewardsExp"] = rewardsExp
    secondBuyItem.ifNotAir {
      tag["SecondBuy"] = secondBuyItem.store()
    }
  }

  override fun load(tag: CompoundTag) {
    buyItem = tag.item("Buy")
    sellItem = tag.item("Sell")
    maxTrades = tag.int("MaxTrades", 7)
    trades = tag.int("Trades")
    rewardsExp = tag.boolean("RewardsExp", true)
    if ("SecondBuy" in tag) {
      secondBuyItem = tag.item("SecondBuy")
    }
  }

}
