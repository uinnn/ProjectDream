package dream.block.property

import korlibs.datastructure.hashCode

/**
 * A Int range block property.
 *
 * This only allow values in range of [range].
 */
class PropertyInt(name: String, val range: IntRange) : AbstractProperty<Int>(name, Int::class) {
  override val allowedValues get() = range

  override fun getName(value: Int): String {
    return value.toString()
  }

  override fun equals(other: Any?): Boolean {
    return when (other) {
      this -> true
      !is PropertyInt -> false
      else -> valueClass == other.valueClass && name == other.name && range == other.range
    }
  }

  override fun hashCode(): Int {
    return hashCode(valueClass, name, range)
  }

  override fun toString(): String {
    return "PropertyInt(name='$name', allowedValues=$range)"
  }
}
