package dream.chat

import kotlinx.serialization.Serializable

/**
 * Represents a text chat component.
 */
@Serializable
data class ComponentSelector(var selector: String) : BaseComponent() {
  override val unformattedText: String get() = selector

  override fun copy() = ComponentSelector(selector).also {
    it.style = style.shallowCopy()
    childrens.forEach(it::add)
  }
}

/**
 * Creates a [ComponentText] by the given [text].
 */
fun selector(text: String) = ComponentSelector(text)

/**
 * Creates a [ComponentText] by the given [text].
 */
inline fun selector(text: String, builder: ComponentSelector.() -> Unit) = ComponentSelector(text).apply(builder)
