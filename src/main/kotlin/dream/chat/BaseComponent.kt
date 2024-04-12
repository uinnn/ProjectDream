package dream.chat

/**
 * Base component style for any chat component.
 */
abstract class BaseComponent : Component {
  override var childrens: MutableList<Component> = ArrayList(4)

  override var style: Style = Style()
    set(value) {
      field = value
      childrens.forEach { it.style.parent(value) }
    }

  override val formatedText: String
    get() = buildString {
      childrens.forEach {
        append(it.style.formatted())
        append(it.unformattedText)
        append(Color.RESET)
      }
    }

  override val unformattedText: String
    get() = buildString {
      childrens.forEach { append(it.unformattedText) }
    }

  override fun add(text: String): Component {
    return add(text(text))
  }

  override fun add(component: Component): Component {
    component.style.parent(RootStyle)
    childrens.add(component)
    return this
  }

  override fun iterator(): Iterator<Component> {
    // in vanilla minecraft, ComponentStyle make a copy for each entry before creating a iterator
    // in dream minecraft software, this not creates a copy
    return (childrens + this).iterator()

    /* Vanilla Minecraft Software
    return (components + this)
       .asSequence()
       .map {
          val copy = it.copy()
          copy.style = copy.style.copy()
          copy
       }
       .iterator()
     */
  }

  override fun hashCode() = 31 * style.hashCode() * childrens.hashCode()
  override fun toString() = "BaseComponent(style=$style, siblings=$childrens)"
  override fun equals(other: Any?): Boolean = when {
    this === other -> true
    other !is BaseComponent -> false
    else -> childrens == other.childrens && style == other.style
  }
}
