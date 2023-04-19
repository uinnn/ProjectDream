package dream.collections

import dream.item.*

/**
 * A list for items.
 *
 * Used to store items on inventory.
 */
class ItemList(size: Int) : ArrayList<ItemStack>(size) {
   
   fun setAir(index: Int) = set(index, ItemStack.AIR)
   
   fun getItemOrNull(index: Int) {
      return
   }
   
   fun getItemOrAir(index: Int): ItemStack {
      return if (index !in 0..size) ItemStack.AIR else super.get(index)
   }
   
   override fun get(index: Int): ItemStack {
      return if (index !in 0..size) ItemStack.AIR else super.get(index)
   }
   
   fun getOrSet(index: Int, selector: () -> ItemStack): ItemStack {
      if (index !in 0..size)
         return selector()
      
      val present = get(index)
      if (!present.isAir)
         return present
      
      return selector().also { set(index, it) }
   }
   
   fun decrease(index: Int, amount: Int, whenDone: Runnable? = null): ItemStack {
      val item = getOrNull(index) ?: return ItemStack.AIR
      return if (item.amount <= amount) {
         setAir(index)
         whenDone?.run()
         item
      } else {
         val splitted = item.split(amount)
         if (item.amount == 0)
            setAir(index)
   
         whenDone?.run()
         splitted
      }
   }
   
   override fun removeAt(index: Int): ItemStack {
      return setAir(index)
   }

   fun setItem(index: Int, item: ItemStack, limit: Int, whenDone: Runnable? = null) {
      set(index, item)
      
      if (!item.isAir && item.amount > limit) {
         item.amount = limit
      }
      
      whenDone?.run()
   }
}
