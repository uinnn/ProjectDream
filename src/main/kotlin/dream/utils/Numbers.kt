package dream.utils

/**
 * Converts this Float to Byte.
 */
fun Float.asByte() = toInt().toByte()

/**
 * Converts this Double to Byte.
 */
fun Double.asByte() = toInt().toByte()

/**
 * Converts this Float to Short.
 */
fun Float.asShort() = toInt().toShort()

/**
 * Converts this Double to Short.
 */
fun Double.asShort() = toInt().toShort()

/**
 * Clamps [value] with [min] and [max].
 */
fun between(min: Int, value: Int, max: Int) = value.coerceIn(min, max)

/**
 * Clamps [value] with [min] and [max].
 */
fun between(min: Long, value: Long, max: Long) = value.coerceIn(min, max)

/**
 * Clamps [value] with [min] and [max].
 */
fun between(min: Float, value: Float, max: Float) = value.coerceIn(min, max)

/**
 * Clamps [value] with [min] and [max].
 */
fun between(min: Double, value: Double, max: Double) = value.coerceIn(min, max)

fun Byte.clamp(min: Byte, max: Byte) = if (this < min) min else if (this > max) max else this
fun Byte.clamp(min: Int, max: Int) = if (this < min) min.toByte() else if (this > max) max.toByte() else this

fun Short.clamp(min: Short, max: Short) = if (this < min) min else if (this > max) max else this
fun Short.clamp(min: Int, max: Int) = if (this < min) min.toShort() else if (this > max) max.toShort() else this
