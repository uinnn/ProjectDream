package dream.chat

import dream.misc.Open
import kotlinx.serialization.Serializable

/**
 * Represents a style for chat.
 */
@Serializable
@Open
class Style {

  private var parent: Style? = null
  private var color: Color? = null
  private var bold: Boolean? = null
  private var italic: Boolean? = null
  private var underlined: Boolean? = null
  private var strikethrough: Boolean? = null
  private var obfuscated: Boolean? = null
  private var clickEvent: ClickEvent? = null
  private var hoverEvent: HoverEvent? = null
  private var insertion: String? = null

  /**
   * Returns if this style is empty.
   */
  val isEmpty: Boolean
    get() = bold == null && italic == null && strikethrough == null && underlined == null && obfuscated == null && clickEvent == null && hoverEvent == null && color == null

  /**
   * Gets the equivalent text formatting code for this style.
   */
  fun formatted(): String {
    if (isEmpty)
      return parent?.formatted() ?: ""

    return buildString {
      if (color != null) append(color)
      if (bold != null) append(Color.BOLD)
      if (italic != null) append(Color.ITALIC)
      if (underlined != null) append(Color.UNDERLINE)
      if (obfuscated != null) append(Color.OBFUSCATED)
      if (strikethrough != null) append(Color.STRIKETHROUGH)
    }
  }

  /**
   * Gets the parent of this style.
   */
  fun parent() = parent
  fun parent(value: Style?): Style {
    parent = value
    return this
  }

  /**
   * Gets the color of this style or the parent color.
   */
  fun color() = color ?: parent?.color
  fun color(value: Color?): Style {
    color = value
    return this
  }

  /**
   * Gets the bold modifier of this style or the parent bold modifier.
   */
  fun bold() = bold ?: parent?.bold == true
  fun bold(value: Boolean?): Style {
    bold = value
    return this
  }

  /**
   * Gets the italic modifier of this style or the parent italic modifier.
   */
  fun italic() = italic ?: parent?.italic == true
  fun italic(value: Boolean?): Style {
    italic = value
    return this
  }

  /**
   * Gets the underlined modifier of this style or the parent underlined modifier.
   */
  fun underlined() = underlined ?: parent?.underlined == true
  fun underlined(value: Boolean?): Style {
    underlined = value
    return this
  }

  /**
   * Gets the strikethrough modifier of this style or the parent strikethrough modifier.
   */
  fun strikethrough() = strikethrough ?: parent?.strikethrough == true
  fun strikethrough(value: Boolean?): Style {
    strikethrough = value
    return this
  }

  /**
   * Gets the obfuscated modifier of this style or the parent obfuscated modifier.
   */
  fun obfuscated() = obfuscated ?: parent?.obfuscated == true
  fun obfuscated(value: Boolean?): Style {
    obfuscated = value
    return this
  }

  /**
   * Gets the click event of this style or the parent click event.
   */
  fun click() = clickEvent ?: parent?.clickEvent
  fun click(value: ClickEvent?): Style {
    clickEvent = value
    return this
  }

  /**
   * Gets the hover event of this style or the parent hover event.
   */
  fun hover() = hoverEvent ?: parent?.hoverEvent
  fun hover(value: HoverEvent?): Style {
    hoverEvent = value
    return this
  }

  /**
   * Gets the insertion of this style or the parent insertion.
   */
  fun insertion() = insertion ?: parent?.insertion
  fun insertion(value: String?): Style {
    insertion = value
    return this
  }

  /**
   * Creates a shallow copy of this style.
   *
   * Changes to this instance's values will not be reflected in the copy, but
   * changes to the parent style's values will be reflected in both this instance and the copy,
   * wherever either does not override a value.
   */
  fun shallowCopy() = Style()
    .bold(bold)
    .italic(italic)
    .strikethrough(strikethrough)
    .obfuscated(obfuscated)
    .underlined(underlined)
    .color(color)
    .click(clickEvent)
    .hover(hoverEvent)
    .insertion(insertion)
    .parent(parent)

  /**
   * Creates a deep copy of this style.
   *
   * No changes to this instance or its parent style will be reflected in the copy.
   */
  fun copy() = Style()
    .bold(bold())
    .italic(italic())
    .strikethrough(strikethrough())
    .obfuscated(obfuscated())
    .underlined(underlined())
    .color(color())
    .click(click())
    .hover(hover())
    .insertion(insertion())
    .parent(parent())
}

/**
 * Root style for any style created.
 *
 * If you want to modify the default style chat messages,
 * you must modify these values.
 */
object RootStyle : Style() {
  override fun toString() = "Style.ROOT"
}
