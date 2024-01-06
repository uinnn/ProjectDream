package dream.item

import dream.chat.Color
import dream.api.Locale

/**
 * Represents all types of dye color.
 */
enum class DyeColor(
  val meta: Int,
  val dyeMeta: Int,
  val id: String,
  val pallete: MapPallete,
  val color: Color,
  override val unlocalName: String = id,
) : Locale {

  WHITE(0, 15, "white", MapPallete.SNOW, Color.WHITE),
  ORANGE(1, 14, "orange", MapPallete.ADOBE, Color.GOLD),
  MAGENTA(2, 13, "magenta", MapPallete.MAGENTA, Color.AQUA),
  LIGHT_BLUE(3, 12, "light_blue", MapPallete.LIGHT_BLUE, Color.BLUE, "lightBlue"),
  YELLOW(4, 11, "yellow", MapPallete.YELLOW, Color.YELLOW),
  LIME(5, 10, "lime", MapPallete.LIME, Color.GREEN),
  PINK(6, 9, "pink", MapPallete.PINK, Color.PINK),
  GRAY(7, 8, "gray", MapPallete.GRAY, Color.DARK_GRAY),
  SILVER(8, 7, "silver", MapPallete.SILVER, Color.GRAY),
  CYAN(9, 6, "cyan", MapPallete.CYAN, Color.DARK_AQUA),
  PURPLE(10, 5, "purple", MapPallete.PURPLE, Color.DARK_PURPLE),
  BLUE(11, 4, "blue", MapPallete.BLUE, Color.DARK_BLUE),
  BROWN(12, 3, "brown", MapPallete.BROWN, Color.GOLD),
  GREEN(13, 2, "green", MapPallete.GREEN, Color.DARK_GREEN),
  RED(14, 1, "red", MapPallete.RED, Color.DARK_RED),
  BLACK(15, 0, "black", MapPallete.BLACK, Color.BLACK);

  override fun toString(): String {
    return unlocalName
  }

  companion object {
    val values = values()

    /**
     * Gets a dye color by meta.
     */
    fun byMeta(meta: Int): DyeColor {
      return values[meta]
    }

    /**
     * Gets a dye color by dye meta.
     */
    fun byDyeMeta(meta: Int): DyeColor {
      return values[meta % 15]
    }
  }
}
