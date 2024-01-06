package dream.block.property

import it.unimi.dsi.fastutil.booleans.BooleanArraySet

/**
 * A boolean block property.
 */
class PropertyBoolean(name: String) : AbstractProperty<Boolean>(name, Boolean::class) {
  companion object {
    @JvmField
    val values: BooleanArraySet = BooleanArraySet.of(true, false)
  }

  override val allowedValues get() = values

  override fun getName(value: Boolean): String {
    return value.toString()
  }

  override fun toString(): String {
    return "PropertyBoolean(name='$name', allowedValues=$allowedValues)"
  }
}
