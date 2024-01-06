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
class RuntimeState(override val block: Block, override val values: PropertyMap) : IState {
  override val properties get() = values.keys

  override fun <T : Any> getValue(property: Property<T>): T {
    return property.valueClass.cast(values[property])
  }

  override fun <T : Any> with(property: Property<T>, value: T): IState {
    return if (values[property] == value) {
      this
    } else {
      values[property] = value
      return this
      
      /*
      val new = HashMap(values)
      new[property] = value
      RuntimeState(block, new)
       */
    }
  }

  override fun toString(): String {
    return "RuntimeState(block=$block, values=${values.values.joinToString()})"
  }

  override fun equals(other: Any?): Boolean {
    return this === other
  }

  override fun hashCode(): Int {
    return values.hashCode()
  }
}
