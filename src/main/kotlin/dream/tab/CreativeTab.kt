package dream.tab

import dream.enchantment.EnchantmentTarget
import dream.api.Locale
import dream.item.*

/**
 * Represents an inventory tab in creative mode.
 */
abstract class CreativeTab(val index: Int, override val unlocalName: String) : Locale {

  /**
   * Texture to use in this tab.
   */
  var texture = "items.png"

  /**
   * If this tab has scrollbar.
   */
  var hasScrollbar = true

  /**
   * Whether to draw the title in the foreground of the tab.
   */
  var drawTitle = true

  /**
   * Enchantments targets for this tab.
   */
  val enchantments = ArrayList<EnchantmentTarget>()

  /**
   * The item icon displayed on this tab.
   */
  val icon: ItemStack = ItemStack(getItemIcon(), 1, getIconData())

  /**
   * Returns the translated label of this tab.
   */
  val localName: String get() = "itemGroup.$unlocalName"

  /**
   * Returns the column of this tab.
   */
  val column: Int get() = index % 6

  /**
   * Returns if this tab is in the first row in the creative inventory.
   */
  val isInFirstRow: Boolean get() = index < 6

  /**
   * Returns the item icon to be displayed on this tab.
   */
  abstract fun getItemIcon(): Item

  /**
   * Returns the item icon data to be displayed on this tab.
   */
  open fun getIconData() = 0

  /**
   * Sets the background texture of this tab.
   */
  fun withTexture(texture: String): CreativeTab {
    this.texture = texture
    return this
  }

  /**
   * Disable the title of this tab.
   */
  fun withoutTitle(): CreativeTab {
    drawTitle = false
    return this
  }

  /**
   * Disable the scrollbar of this tab.
   */
  fun withoutScrollbar(): CreativeTab {
    hasScrollbar = false
    return this
  }

  /**
   * Sets the enchantments target of this tab.
   */
  fun targets(vararg targets: EnchantmentTarget): CreativeTab {
    enchantments.addAll(targets)
    return this
  }

  init {
    TABS[index] = this
  }

  companion object Registry {

    /**
     * All creative tabs.
     */
    @JvmField
    val TABS = ArrayList<CreativeTab>(12)

    @JvmField
    val BLOCKS = tab(0, "buildingBlocks", Items.AIR)
    @JvmField
    val DECORATIONS = tab(1, "decorations", Items.AIR)
    @JvmField
    val REDSTONE = tab(2, "redstone", Items.AIR)
    @JvmField
    val TRANSPORT = tab(3, "transportation", Items.AIR)
    @JvmField
    val MISC = tab(4, "misc", Items.AIR)
    @JvmField
    val SEARCH = tab(5, "search", Items.AIR)
    @JvmField
    val FOODS = tab(6, "food", Items.AIR)
    @JvmField
    val TOOLS = tab(7, "tools", Items.AIR)
    @JvmField
    val COMBAT = tab(8, "combat", Items.AIR)
    @JvmField
    val BREWING = tab(9, "brewing", Items.AIR)
    @JvmField
    val MATERIALS = tab(10, "materials", Items.AIR)
    @JvmField
    val INVENTORY = tab(11, "inventory", Items.AIR)
  }
}

/**
 * Creates a new creative tab by given data.
 */
fun tab(index: Int, name: String, item: Item, metadata: Int = 0): CreativeTab {
  return object : CreativeTab(index, name) {
    override fun getItemIcon() = item
    override fun getIconData() = metadata
  }
}
