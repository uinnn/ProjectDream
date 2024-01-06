package dream.item

import dream.misc.*

/**
 * Represents a color pallete to display in Maps items.
 */
@Open
data class MapPallete(val index: Int, var color: Int) {

  /**
   * Gets red color data of this pallete.
   */
  fun getRed(alpha: Int) = (color shr 16 and 255) * alpha / 255

  /**
   * Gets green color data of this pallete.
   */
  fun getGreen(alpha: Int) = (color shr 8 and 255) * alpha / 255

  /**
   * Gets blue color data of this pallete.
   */
  fun getBlue(alpha: Int) = (color and 255) * alpha / 255

  /**
   * Gets alpha color data of this pallete.
   */
  fun getAlpha(state: Int) = when (state) {
    0 -> 180
    2 -> 255
    3 -> 135
    else -> 220
  }

  /**
   * Gets the RGB data valued in a [Int] with a specific [state].
   */
  fun getRGB(state: Int): Int {
    val alpha = getAlpha(state)
    return -16777216 or getRed(alpha) shl 16 or getGreen(alpha) shl 8 or getBlue(alpha)
  }

  /**
   * Register this pallete.
   */
  fun register(): MapPallete {
    COLORS += this
    return this
  }

  companion object {
    @JvmField
    val COLORS = ArrayList<MapPallete>(36)
    @JvmField
    val AIR = MapPallete(0, 0).register()
    @JvmField
    val GRASS = MapPallete(1, 8368696).register()
    @JvmField
    val SAND = MapPallete(2, 16247203).register()
    @JvmField
    val CLOTH = MapPallete(3, 13092807).register()
    @JvmField
    val TNT = MapPallete(4, 16711680).register()
    @JvmField
    val ICE = MapPallete(5, 10526975).register()
    @JvmField
    val IRON = MapPallete(6, 10987431).register()
    @JvmField
    val FOLIAGE = MapPallete(7, 31744).register()
    @JvmField
    val SNOW = MapPallete(8, 16777215).register()
    @JvmField
    val CLAY = MapPallete(9, 10791096).register()
    @JvmField
    val DIRT = MapPallete(10, 9923917).register()
    @JvmField
    val STONE = MapPallete(11, 7368816).register()
    @JvmField
    val WATER = MapPallete(12, 4210943).register()
    @JvmField
    val WOOD = MapPallete(13, 9402184).register()
    @JvmField
    val QUARTZ = MapPallete(14, 16776437).register()
    @JvmField
    val ADOBE = MapPallete(15, 14188339).register()
    @JvmField
    val MAGENTA = MapPallete(16, 11685080).register()
    @JvmField
    val LIGHT_BLUE = MapPallete(17, 6724056).register()
    @JvmField
    val YELLOW = MapPallete(18, 15066419).register()
    @JvmField
    val LIME = MapPallete(19, 8375321).register()
    @JvmField
    val PINK = MapPallete(20, 15892389).register()
    @JvmField
    val GRAY = MapPallete(21, 5000268).register()
    @JvmField
    val SILVER = MapPallete(22, 10066329).register()
    @JvmField
    val CYAN = MapPallete(23, 5013401).register()
    @JvmField
    val PURPLE = MapPallete(24, 8339378).register()
    @JvmField
    val BLUE = MapPallete(25, 3361970).register()
    @JvmField
    val BROWN = MapPallete(26, 6704179).register()
    @JvmField
    val GREEN = MapPallete(27, 6717235).register()
    @JvmField
    val RED = MapPallete(28, 10040115).register()
    @JvmField
    val BLACK = MapPallete(29, 1644825).register()
    @JvmField
    val GOLD = MapPallete(30, 16445005).register()
    @JvmField
    val DIAMOND = MapPallete(31, 6085589).register()
    @JvmField
    val LAPIS = MapPallete(32, 4882687).register()
    @JvmField
    val EMERALD = MapPallete(33, 55610).register()
    @JvmField
    val OBSIDIAN = MapPallete(34, 8476209).register()
    @JvmField
    val NETHERRACK = MapPallete(35, 7340544).register()
  }
}
