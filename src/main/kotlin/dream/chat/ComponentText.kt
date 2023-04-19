package dream.chat

import kotlinx.serialization.*

/**
 * Represents a text chat component.
 */
@Serializable
data class ComponentText(var text: String) : ComponentStyle() {
   override val unformattedText: String get() = text
   
   override fun copy() = ComponentText(text).also {
      it.style = style.shallowCopy()
      components.forEach(it::add)
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
