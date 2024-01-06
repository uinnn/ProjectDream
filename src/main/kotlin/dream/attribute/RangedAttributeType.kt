package dream.attribute

import dream.utils.*

/**
 * Class representing a ranged attribute.
 *
 * The [RangedAttributeType] class extends the [AttributeType] class and represents an attribute
 * with a specified range of valid values.
 *
 * @param unlocalName The unlocalized name of the attribute.
 * @param defaultValue The default value of the attribute.
 * @param range The range of valid values for the attribute.
 * @param description The description of the attribute. Defaults to an empty string.
 * @param children The children of the attribute, if any. Defaults to `null`.
 * @param shouldWatch Indicates whether the attribute should be watched for changes. Defaults to `false`.
 *
 * @throws IllegalArgumentException if the default value is not within the specified range.
 */
class RangedAttributeType(
  unlocalName: String,
  defaultValue: Double,
  val range: DoubleRange,
  var description: String = "",
  children: AttributeType? = null,
  shouldWatch: Boolean = false,
) : AttributeType(unlocalName, defaultValue, children, shouldWatch) {
  
  init {
    require(defaultValue in range) { "$defaultValue is not in range $range of a ranged attribute" }
  }
  
  /**
   * Clamps the specified value within the valid range.
   *
   * @param value The value to be clamped.
   * @return The clamped value within the valid range.
   */
  override fun clampValue(value: Double): Double {
    return value.coerceIn(range)
  }
}
