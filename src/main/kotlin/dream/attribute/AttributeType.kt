package dream.attribute

import dream.misc.*

/**
 * Abstract class representing an attribute.
 *
 * The [AttributeType] class provides a base implementation for attributes.
 *
 * @param unlocalName The unlocalized name of the attribute.
 * @param defaultValue The default value of the attribute.
 * @param children The children of the attribute, if any. Defaults to `null`.
 * @param shouldWatch Indicates whether the attribute should be watched for changes. Defaults to `false`.
 */
@Open
class AttributeType(
  val unlocalName: String,
  var defaultValue: Double,
  val children: AttributeType? = null,
  var shouldWatch: Boolean = false,
) : Comparable<AttributeType> {
  
  /**
   * Clamps the specified value by returning it as is.
   *
   * @param value The value to be clamped.
   * @return The unmodified value.
   */
  fun clampValue(value: Double): Double {
    return value
  }
  
  override fun compareTo(other: AttributeType): Int = unlocalName.compareTo(other.unlocalName)
  override fun hashCode(): Int = unlocalName.hashCode()
  override fun equals(other: Any?): Boolean = if (other is AttributeType) unlocalName == other.unlocalName else false
}
