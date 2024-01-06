package dream.block.property

import com.google.common.collect.Sets
import dream.api.Id
import dream.utils.enumSet
import kotlin.reflect.KClass

/**
 * A enum block property.
 */
class PropertyEnum<T>(
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
  val lookup = allowedValues.associateBy { it.id }

  override fun getName(value: T): String {
    return value.id
  }

  override fun toString(): String {
    return "PropertyEnum(name='$name', allowedValues=$allowedValues)"
  }
}
