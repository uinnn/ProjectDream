package dream.block.property

/**
 * A string block property.
 */
class PropertyString(name: String) : AbstractProperty<String>(name, String::class) {
  override val allowedValues get() = emptyList<String>()

  override fun getName(value: String): String {
    return value
  }

  override fun toString(): String {
    return "PropertyString(name='$name')"
  }
}
