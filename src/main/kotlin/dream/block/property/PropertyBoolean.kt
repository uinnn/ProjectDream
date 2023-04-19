package dream.block.property

import it.unimi.dsi.fastutil.booleans.*

/**
 * A boolean block property.
 */
public class PropertyBoolean(name: String) : AbstractProperty<Boolean>(name, Boolean::class) {
   companion object {
      @JvmField public val values: BooleanArraySet = BooleanArraySet.of(true, false)
   }
   
   public override val allowedValues get() = values
   
   public override fun getName(value: Boolean): String {
      return value.toString()
   }
   
   override fun toString(): String {
      return "PropertyBoolean(name='$name', allowedValues=$allowedValues)"
   }
}
