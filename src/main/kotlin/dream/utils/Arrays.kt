package dream.utils

import it.unimi.dsi.fastutil.io.*

fun <K, V> Array<out Pair<K, V>>.toMutableMap(): MutableMap<K, V> = toMap(LinkedHashMap())
fun <K, V> Array<out Pair<K, V>>.toHashMap(): HashMap<K, V> = toMap(HashMap())
fun <K, V> Array<out Pair<K, V>>.toLinkedHashMap(): LinkedHashMap<K, V> = toMap(LinkedHashMap())

/**
 * Creates a [FastByteArrayInputStream] based on this array.
 */
fun ByteArray.fastInputStream() = FastByteArrayInputStream(this)

/**
 * Creates a [FastByteArrayOutputStream] based on this array.
 */
fun ByteArray.fastOutputStream() = FastByteArrayOutputStream(this)

/**
 * Maps this array to [R].
 */
inline fun <T, reified R> Array<T>.mapArray(action: (T) -> R): Array<R> {
  return Array(size) {
    action(get(it))
  }
}

/**
 * Maps this array to [R].
 */
inline fun <T, reified R> Array<T>.mapArrayIndexed(action: (Int, T) -> R): Array<R> {
  return Array(size) {
    action(it, get(it))
  }
}


/**
 * Creates a new array of a specific size and copies elements from the original array to the new array.
 * If the new size is greater than the original size, the remaining elements are filled with the specified fill function.
 *
 * @param newSize The size of the new array.
 * @param fill The function that provides the value to fill the remaining elements with.
 * @return The new array with copied elements from the original array and filled elements.
 */
inline fun <reified T> Array<T>.copyOf(newSize: Int, fill: () -> T): Array<T> {
  val srcSize = size
  return Array(newSize) { index -> if (index < srcSize) this[index] else fill() }
}
