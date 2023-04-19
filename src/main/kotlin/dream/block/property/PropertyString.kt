package dream.block.property

/**
 * A string block property.
 */
public class PropertyString(name: String) : AbstractProperty<String>(name, String::class) {
   public override val allowedValues get() = emptyList<String>()
   
   public override fun getName(value: String): String {
      return value
   }
   
   override fun toString(): String {
      return "PropertyString(name='$name')"
   }
}
