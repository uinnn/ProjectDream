package dream.block.property

import dream.api.*
import kotlin.reflect.*

typealias Prop = Property<out Any>
typealias PropertyMap = MutableMap<Prop, Any>

/**
 * Represents a block property.
 */
interface Property<T : Any> : Nameable, Comparable<Property<T>> {

  /**
   * Gets the value class for this property.
   */
  val valueClass: KClass<T>

  /**
   * All allowed values for this property.
   */
  val allowedValues: Iterable<T>

  /**
   * Gets the name for the specific [value].
   */
  fun getName(value: T): String

  override fun compareTo(other: Property<T>): Int {
    return name.compareTo(other.name)
  }
}
