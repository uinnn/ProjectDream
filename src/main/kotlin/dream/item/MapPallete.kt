package dream.item

import dream.misc.*

/**
 * Represents a color pallete to display in Maps items.
 */
@Open
public data class MapPallete(val index: Int, var color: Int) {
   
   /**
    * Gets red color data of this pallete.
    */
   public fun getRed(alpha: Int) = (color shr 16 and 255) * alpha / 255
   
   /**
    * Gets green color data of this pallete.
    */
   public fun getGreen(alpha: Int) = (color shr 8 and 255) * alpha / 255
   
   /**
    * Gets blue color data of this pallete.
    */
   public fun getBlue(alpha: Int) = (color and 255) * alpha / 255
   
   /**
    * Gets alpha color data of this pallete.
    */
   public fun getAlpha(state: Int) = when (state) {
      0 -> 180
      2 -> 255
      3 -> 135
      else -> 220
   }
   
   /**
    * Gets the RGB data valued in a [Int] with a specific [state].
    */
   public fun getRGB(state: Int): Int {
      val alpha = getAlpha(state)
      return -16777216 or getRed(alpha) shl 16 or getGreen(alpha) shl 8 or getBlue(alpha)
   }
   
   /**
    * Register this pallete.
    */
   public fun register(): MapPallete {
      COLORS += this
      return this
   }
   
   companion object {
      @JvmField public val COLORS = ArrayList<MapPallete>(36)
      @JvmField public val AIR = MapPallete(0, 0).register()
      @JvmField public val GRASS = MapPallete(1, 8368696).register()
      @JvmField public val SAND = MapPallete(2, 16247203).register()
      @JvmField public val CLOTH = MapPallete(3, 13092807).register()
      @JvmField public val TNT = MapPallete(4, 16711680).register()
      @JvmField public val ICE = MapPallete(5, 10526975).register()
      @JvmField public val IRON = MapPallete(6, 10987431).register()
      @JvmField public val FOLIAGE = MapPallete(7, 31744).register()
      @JvmField public val SNOW = MapPallete(8, 16777215).register()
      @JvmField public val CLAY = MapPallete(9, 10791096).register()
      @JvmField public val DIRT = MapPallete(10, 9923917).register()
      @JvmField public val STONE = MapPallete(11, 7368816).register()
      @JvmField public val WATER = MapPallete(12, 4210943).register()
      @JvmField public val WOOD = MapPallete(13, 9402184).register()
      @JvmField public val QUARTZ = MapPallete(14, 16776437).register()
      @JvmField public val ADOBE = MapPallete(15, 14188339).register()
      @JvmField public val MAGENTA = MapPallete(16, 11685080).register()
      @JvmField public val LIGHT_BLUE = MapPallete(17, 6724056).register()
      @JvmField public val YELLOW = MapPallete(18, 15066419).register()
      @JvmField public val LIME = MapPallete(19, 8375321).register()
      @JvmField public val PINK = MapPallete(20, 15892389).register()
      @JvmField public val GRAY = MapPallete(21, 5000268).register()
      @JvmField public val SILVER = MapPallete(22, 10066329).register()
      @JvmField public val CYAN = MapPallete(23, 5013401).register()
      @JvmField public val PURPLE = MapPallete(24, 8339378).register()
      @JvmField public val BLUE = MapPallete(25, 3361970).register()
      @JvmField public val BROWN = MapPallete(26, 6704179).register()
      @JvmField public val GREEN = MapPallete(27, 6717235).register()
      @JvmField public val RED = MapPallete(28, 10040115).register()
      @JvmField public val BLACK = MapPallete(29, 1644825).register()
      @JvmField public val GOLD = MapPallete(30, 16445005).register()
      @JvmField public val DIAMOND = MapPallete(31, 6085589).register()
      @JvmField public val LAPIS = MapPallete(32, 4882687).register()
      @JvmField public val EMERALD = MapPallete(33, 55610).register()
      @JvmField public val OBSIDIAN = MapPallete(34, 8476209).register()
      @JvmField public val NETHERRACK = MapPallete(35, 7340544).register()
   }
}
