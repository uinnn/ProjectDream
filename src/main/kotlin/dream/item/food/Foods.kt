package dream.item.food

import dream.effect.*

/**
 * All default foods.
 */
object Foods {
   @JvmField
   val APPLE = Food(4, 0.3f)
   
   @JvmField
   val BAKED_POTATO = Food(5, 0.6f)
   
   @JvmField
   val BEEF = Food(3, 0.3f, true)
   
   @JvmField
   val BREAD = Food(5, 0.6f)
   
   @JvmField
   val CARROT = Food(3, 0.6f)
   
   @JvmField
   val CHICKEN = Food(2, 0.3f).meat().effect(Effect() to 0.3f)
   
   @JvmField
   val COOKED_BEEF = Food(8, 0.8f).meat()
   
   @JvmField
   val COOKED_CHICKEN = Food(6, 0.6f).meat()
   
   @JvmField
   val COOKED_MUTTON = Food(6, 0.8f).meat()
   
   @JvmField
   val COOKED_PORKCHOP = Food(8, 0.8f).meat()
   
   @JvmField
   val COOKED_RABBIT = Food(5, 0.6f).meat()
   
   @JvmField
   val COOKED_SALMON = Food(6, 0.8f).meat()
   
   @JvmField
   val COOKIE = Food(2, 0.1f)
   
   @JvmField
   val ENCHANTED_GOLDEN_APPLE = Food(4, 1.2f)
      .alwaysEat()
   
   @JvmField
   val GOLDEN_APPLE = Food(4, 1.2f)
      .alwaysEat()
   
   @JvmField
   val GOLDEN_CARROT = Food(4, 0.3f)
   
   @JvmField
   val MELON_SLICE = Food(4, 0.3f)
   
   @JvmField
   val MUTTON = Food(4, 0.3f).meat()
   
   @JvmField
   val POISONOUS_POTATO = Food(4, 0.3f)
   
   @JvmField
   val PORKCHOP = Food(4, 0.3f).meat()
   
   @JvmField
   val POTATO = Food(4, 0.3f)
   
   @JvmField
   val PUFFERFISH = Food(4, 0.3f)
   
   @JvmField
   val PUMPKIN_PIE = Food(4, 0.3f)
   
   @JvmField
   val RABBIT = Food(4, 0.3f).meat()
   
   @JvmField
   val RABBIT_STEW = Food(4, 0.3f)
   
   @JvmField
   val ROTTEN_FLESH = Food(4, 0.3f).meat()
   
   @JvmField
   val SALMON = Food(4, 0.3f).meat()
   
   @JvmField
   val SPIDER_EYE = Food(4, 0.3f)
}
