package dream.block.property

import com.google.common.collect.*
import dream.interfaces.*
import dream.utils.*
import kotlin.reflect.*

/**
 * A enum block property.
 */
public class PropertyEnum<T>(
   name: String,
   valueClass: KClass<T>,
   override val allowedValues: Iterable<T>,
) : AbstractProperty<T>(name, valueClass) where T : Enum<T>, T : Id {
   
   constructor(name: String, valueClass: KClass<T>) :
      this(name, valueClass, enumSet(valueClass))
   
   constructor(name: String, valueClass: KClass<T>, predicate: (T) -> Boolean) :
      this(name, valueClass, Sets.filter(enumSet(valueClass), predicate))
   
   /**
    * All allowed values associated by their id.
    */
   public val lookup = allowedValues.associateBy { it.id }
   
   public override fun getName(value: T): String {
      return value.id
   }
   
   override fun toString(): String {
      return "PropertyEnum(name='$name', allowedValues=$allowedValues)"
   }
}
