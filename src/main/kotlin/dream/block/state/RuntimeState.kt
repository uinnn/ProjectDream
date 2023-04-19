package dream.block.state

import dream.block.*
import dream.block.property.*
import kotlin.reflect.*

/**
 * Represents a runtime [IState].
 *
 * The mainly difference of [RuntimeState] to [State] is that
 * [RuntimeState] creates a new state every time he needs a new one.
 *
 * [State] caches all predefined states in a [Table] and gets them when he needs.
 */
public class RuntimeState(override val block: Block, override val values: PropertyMap) : IState {
   public override val properties get() = values.keys
   
   override fun <T : Any> getValue(property: Property<T>): T {
      return property.valueClass.cast(values[property])
   }
   
   override fun <T : Any> with(property: Property<T>, value: T): IState {
      return if (values[property] == value) {
         this
      } else {
         val new = HashMap(values)
         new[property] = value
         RuntimeState(block, new)
      }
   }
   
   public override fun toString(): String {
      return "RuntimeState(block=$block, values=${values.values.joinToString()})"
   }
   
   public override fun equals(other: Any?): Boolean {
      return this === other
   }
   
   public override fun hashCode(): Int {
      return values.hashCode()
   }
}
