package dream.utils

import it.unimi.dsi.fastutil.io.*
import java.util.EnumSet

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
