package dream.item.food

import dream.entity.player.*
import dream.item.*
import dream.level.*
import dream.tab.*

/**
 * Represents a food item.
 */
class ItemFood(val food: Food) : Item(CreativeTab.FOODS) {
   
   /**
    * Called after [player] eats this food.
    */
   fun onEaten(item: ItemStack, player: Player) {
      food.bidEffects(player)
   }
   
   override fun onFinishUse(level: Level, item: ItemStack, player: Player): ItemStack {
      item.amount--
      player.eat(food)
      onEaten(item, player)
      return item
   }
   
   override fun onRightClick(level: Level, item: ItemStack, player: Player): ItemStack {
      if (player.canEat(food.canAlwaysEat))
         player.setItemInUse(item, getUseDuration(item))
      
      return item
   }
   
   override fun getUseDuration(item: ItemStack): Int {
      return food.eatDuration
   }
   
   override fun getAnimation(item: ItemStack): ItemAnimation {
      return ItemAnimation.EAT
   }
}
