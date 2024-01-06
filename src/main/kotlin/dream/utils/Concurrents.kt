package dream.utils

import com.google.common.util.concurrent.AtomicDouble
import java.util.concurrent.atomic.*

/**
 * Creates a atomic reference by [value].
 */
fun <T> atomic(value: T) = AtomicReference(value)

/**
 * Creates a atomic boolean reference.
 */
fun Boolean.atomic() = AtomicBoolean(this)

/**
 * Creates a atomic int reference.
 */
fun Int.atomic() = AtomicInteger(this)

/**
 * Creates a atomic long reference.
 */
fun Long.atomic() = AtomicLong(this)

/**
 * Creates a atomic double reference.
 */
fun Double.atomic() = AtomicDouble(this)
