package dream.chat

/**
 * Base component style for any chat component.
 */
abstract class ComponentStyle : Component {
   override val components: MutableList<Component> = ArrayList()
   
   override var style: Style = Style()
      set(value) {
         field = value
         components.forEach { it.style.parent(value) }
      }
   
   override val formatedText: String
      get() = buildString {
         components.forEach {
            append(it.style.formatted())
            append(it.unformattedText)
            append(Color.RESET)
         }
      }
   
   override val unformattedText: String
      get() = buildString {
         components.forEach { append(it.unformattedText) }
      }
   
   override fun add(text: String): Component {
      return add(text(text))
   }
   
   override fun add(component: Component): Component {
      component.style.parent(RootStyle)
      components.add(component)
      return this
   }
   
   override fun iterator(): Iterator<Component> {
      // in vanilla minecraft, ComponentStyle make a copy for each entry before creating a iterator
      // in dream minecraft software, this not creates a copy
      return (components + this).iterator()
      
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
   
   override fun hashCode() = 31 * style.hashCode() * components.hashCode()
   override fun toString() = "ComponentStyle(style=$style, siblings=$components)"
   override fun equals(other: Any?): Boolean = when {
      this === other -> true
      other !is ComponentStyle -> false
      else -> components == other.components && style == other.style
   }
}
