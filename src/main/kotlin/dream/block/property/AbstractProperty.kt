package dream.block.property

import com.soywiz.kds.*
import dream.misc.*
import kotlin.reflect.*

/**
 * A base property implementation.
 *
 * Any other property must extend this class.
 */
@Open
public abstract class AbstractProperty<T : Any>(
   override val name: String,
   override val valueClass: KClass<T>,
) : Property<T> {
   
   override fun toString(): String {
      return "Property(name='$name', class=$valueClass, allowedValues=${allowedValues.joinToString()})"
   }
   
   override fun equals(other: Any?): Boolean {
      return when (other) {
         this -> true
         !is Property<*> -> false
         else -> valueClass == other.valueClass && name == other.name
      }
   }
   
   override fun hashCode(): Int {
      return hashCode(valueClass, name)
   }
}
