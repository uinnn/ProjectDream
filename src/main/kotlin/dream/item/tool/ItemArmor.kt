package dream.item.tool

import dream.item.*
import dream.tab.*
import java.awt.*

/**
 * Represents an armor item.
 */
class ItemArmor(
  var material: ArmorMaterial,
  var renderIndex: Int,
  var type: ArmorType,
) : Item(CreativeTab.COMBAT) {

  /**
   * Gets the defense of this armor.
   */
  val defense get() = material.getDefense(type)

  /**
   * Gets the durability of this armor
   */
  val durability get() = material.getDurability(type)

  /**
   * Gets if the type of this armor is helmet.
   */
  val isHelmet get() = type == ArmorType.HELMET

  /**
   * Gets if the type of this armor is chestplate.
   */
  val isChestplate get() = type == ArmorType.CHESTPLATE

  /**
   * Gets if the type of this armor is leggings.
   */
  val isLeggings get() = type == ArmorType.LEGGINGS

  /**
   * Gets if the type of this armor is boots.
   */
  val isBoots get() = type == ArmorType.BOOTS

  /**
   * Returns if this armor has color on [item].
   */
  fun hasColor(item: ItemStack): Boolean {
    return if (!material.hasColor) false else item.hasColor
  }

  /**
   * Returns the armor color on [item].
   */
  fun getColorRGB(item: ItemStack): Int {
    return if (!material.hasColor) -1 else item.color
  }

  /**
   * Returns the armor color on [item].
   */
  fun getColor(item: ItemStack): Color {
    return Color(getColorRGB(item))
  }

  /**
   * Removes the armor color on [item].
   */
  fun removeColor(item: ItemStack) {
    if (material.hasColor) {
      item.removeDisplay("color")
    }
  }

  override fun getEnchantability(): Int {
    return material.enchantability
  }

  override fun initialize() {
    maxDurability = material.getDurability(type)
    maxStack = 1
  }

  companion object {

    /**
     * The default leather color.
     */
    const val LEATHER_COLOR = 10511680
  }
}
