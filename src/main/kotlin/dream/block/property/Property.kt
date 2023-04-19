package dream.block.property

import dream.interfaces.*
import kotlin.reflect.*

typealias Prop = Property<out Any>
typealias PropertyMap = Map<Prop, Any>

/**
 * Represents a block property.
 */
public interface Property<T : Any> : Nameable, Comparable<Property<T>> {
   
   /**
    * Gets the value class for this property.
    */
   public val valueClass: KClass<T>
   
   /**
    * All allowed values for this property.
    */
   public val allowedValues: Iterable<T>
   
   /**
    * Gets the name for the specific [value].
    */
   public fun getName(value: T): String
   
   public override fun compareTo(other: Property<T>): Int {
      return name.compareTo(other.name)
   }
}
