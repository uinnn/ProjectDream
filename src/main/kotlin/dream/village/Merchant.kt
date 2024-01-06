package dream.village

import dream.entity.player.*
import dream.item.*

/**
 * An merchant. Merchant can be used to trade items to another items.
 */
interface Merchant {

  /**
   * The customer of this merchant.
   */
  var customer: Player

  /**
   * Gets the trade recipes for this merchant.
   */
  var trades: MerchantTradeList

  /**
   * Called when player successfully trade with this merchant.
   */
  fun useTrade(trade: MerchantTrade) {
    trade.onTrade(this)
  }

  /**
   * Notifies the merchant about the traded item.
   *
   * @param sellItem - if failed on trade, it's AIR item
   */
  fun notifyMerchant(sellItem: ItemStack)

}
