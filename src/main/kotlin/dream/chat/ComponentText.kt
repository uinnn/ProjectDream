package dream.chat

import kotlinx.serialization.Serializable

/**
 * Represents a text chat component.
 */
@Serializable
data class ComponentText(var text: String) : BaseComponent() {
  override val unformattedText: String get() = text

  override fun copy() = ComponentText(text).also {
    it.style = style.shallowCopy()
    childrens.forEach(it::add)
  }

  override fun toJson(): String {
    return ChatJson.encodeToString(serializer(), this)
  }
}

/**
 * Creates a [ComponentText] by the given [text].
 */
fun text(text: String) = ComponentText(text)

/**
 * Creates a [ComponentText] by the given [text].
 */
inline fun text(text: String, builder: ComponentText.() -> Unit) = ComponentText(text).apply(builder)
