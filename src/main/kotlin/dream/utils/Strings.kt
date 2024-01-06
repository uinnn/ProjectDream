package dream.utils

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Converts this string to a byte or [default].
 */
fun String.toByteOrDefault(default: Byte = 0) = toByteOrNull() ?: default

/**
 * Converts this string to a short or [default].
 */
fun String.toShortOrDefault(default: Short = 0) = toShortOrNull() ?: default

/**
 * Converts this string to an int or [default].
 */
fun String.toIntOrDefault(default: Int = 0) = toIntOrNull() ?: default

/**
 * Converts this string to a long or [default].
 */
fun String.toLongOrDefault(default: Long = 0) = toLongOrNull() ?: default

/**
 * Converts this string to a float or [default].
 */
fun String.toFloatOrDefault(default: Float = 0f) = toFloatOrNull() ?: default

/**
 * Converts this string to a double or [default].
 */
fun String.toDoubleOrDefault(default: Double = 0.0) = toDoubleOrNull() ?: default

/**
 * Creates a ``toString`` builder.
 */
inline fun makeString(obj: Any, builder: ToStringBuilder.() -> Unit): String {
  return ToStringBuilder(obj).apply(builder).build()
}
