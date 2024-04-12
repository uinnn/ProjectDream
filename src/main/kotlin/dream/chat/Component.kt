package dream.chat

import dream.item.ItemStack
import dream.misc.Open
import kotlinx.serialization.encodeToString

/**
 * Base top-level interface for any chat component.
 */
@Open
interface Component : Iterable<Component> {

  /**
   * The style of this component.
   */
  var style: Style

  /**
   * Get the unformatted text of this component.
   */
  val unformattedText: String

  /**
   * Get the formatted text of this component.
   */
  val formatedText: String

  /**
   * Gets all subcomponent of this root component.
   */
  var childrens: MutableList<Component>

  /**
   * Gets if this component is a empty component.
   */
  val isEmpty: Boolean
    get() = this === EMPTY

  /**
   * Appends the given text to the end of this component.
   */
  fun add(text: String): Component

  /**
   * Appends the given component to the end of this component.
   */
  fun add(component: Component): Component

  /**
   * Creates a copy of this component.
   */
  fun copy(): Component

  /**
   * Stringify this component to Json.
   */
  fun toJson(): String {
    return ChatJson.encodeToString(this)
  }

  companion object {

    /**
     * An empty component.
     */
    val EMPTY = ComponentText("")

    /**
     * Deserializes a component by the given [string].
     */
    fun fromString(string: String): Component {
      val parser = ChatJson

      return runCatching {
        parser.decodeFromString<ComponentText>(string)
      }.recoverCatching {
        parser.decodeFromString<ComponentScore>(string)
      }.recoverCatching {
        parser.decodeFromString<ComponentSelector>(string)
      }.getOrThrow()
    }
  }
}

/**
 * Sets the hover event for this component.
 */
fun Component.hover(event: HoverEvent?): Component {
  style.hover(event)
  return this
}

/**
 * Sets the hover event to show text for this component.
 */
fun Component.showText(value: String) = hover(TextHover(value))

/**
 * Sets the hover event to show achievement for this component.
 */
fun Component.showAchievement(value: String) = hover(AchievementHover(value))

/**
 * Sets the hover event to show item for this component.
 */
fun Component.showItem(value: String) = hover(ItemHover(value))

/**
 * Sets the hover event to show item for this component.
 */
fun Component.showItem(value: ItemStack) = hover(ItemHover(value))

/**
 * Sets the hover event to show entity for this component.
 */
fun Component.showEntity(value: String) = hover(EntityHover(value))

/**
 * Sets the click event for this component.
 */
fun Component.click(event: ClickEvent?): Component {
  style.click(event)
  return this
}

/**
 * Sets the click event to open url for this component.
 */
fun Component.openUrl(value: String) = click(OpenUrlClick(value))

/**
 * Sets the click event to run command for this component.
 */
fun Component.run(value: String) = click(RunCommandClick(value))

/**
 * Sets the click event to suggest command for this component.
 */
fun Component.suggest(value: String) = click(SuggestCommandClick(value))

/**
 * Sets the click event to change page for this component.
 */
fun Component.changePage(value: String) = click(ChangePageClick(value))

/**
 * Sets the click event to open file for this component.
 */
fun Component.openFile(value: String) = click(OpenFileClick(value))

/**
 * Sets the click event to show twitch user info for this component.
 */
fun Component.twitchInfo(value: String) = click(TwitchInfoClick(value))

/**
 * Sets the color for this component.
 */
fun Component.color(color: Color?): Component {
  style.color(color)
  return this
}
