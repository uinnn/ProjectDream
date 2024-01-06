package dream.item.misc

import dream.item.*

class ItemBook : Item() {
  override fun isTool(item: ItemStack): Boolean {
    return item.amount == 1
  }

  override fun getEnchantability(): Int {
    return 1
  }
}
