package dream.entity.passive

import dream.entity.*
import dream.entity.base.*
import dream.entity.player.*
import dream.item.*
import dream.village.*

class Villager(override var customer: Player, override var trades: MerchantTradeList) : EntityAgeable(), Merchant, NPC {
  override fun notifyMerchant(sellItem: ItemStack) {

  }

}
