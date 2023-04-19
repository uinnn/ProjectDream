package dream.block.property

import com.soywiz.kds.*

/**
 * A Int range block property.
 *
 * This only allow values in range of [range].
 */
public class PropertyInt(name: String, val range: IntRange) : AbstractProperty<Int>(name, Int::class) {
   public override val allowedValues get() = range
   
   public override fun getName(value: Int): String {
      return value.toString()
   }
   
   public override fun equals(other: Any?): Boolean {
      return when (other) {
         this -> true
         !is PropertyInt -> false
         else -> valueClass == other.valueClass && name == other.name && range == other.range
      }
   }
   
   public override fun hashCode(): Int {
      return hashCode(valueClass, name, range)
   }
   
   override fun toString(): String {
      return "PropertyInt(name='$name', allowedValues=$range)"
   }
}
