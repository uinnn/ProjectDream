package dream.chat

import kotlinx.serialization.*

/**
 * Represents a scoreboard chat component.
 */
@Serializable
data class ComponentScore(
   var name: String,
   var objective: String,
   var value: String = "",
) : ComponentStyle() {
   
   // TODO: Add support for [ComponentScore] and Scoreboard
   override val unformattedText: String
      get() = TODO()
   
   override fun copy() = ComponentScore(name, objective, value).also {
      it.style = style.shallowCopy()
      components.forEach(it::add)
   }
}

/**
 * Creates a [ComponentScore] by the given values.
 */
fun score(name: String, objective: String, value: String = "") = ComponentScore(name, objective, value)

/**
 * Creates a [ComponentScore] by the given values.
 */
inline fun score(name: String, objective: String, value: String = "", builder: ComponentScore.() -> Unit) =
   ComponentScore(name, objective, value).apply(builder)
