package dream.item

import dream.chat.Color

/**
 * All types of rarity that's an item can be.
 */
enum class ItemRarity(val color: Color, val display: String) {
  COMMON(Color.WHITE, "Common"),
  UNCOMMON(Color.YELLOW, "Uncommon"),
  RARE(Color.AQUA, "Rare"),
  EPIC(Color.PINK, "Epic")
}
