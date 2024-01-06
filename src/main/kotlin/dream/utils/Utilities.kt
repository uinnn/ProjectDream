@file:Suppress("NOTHING_TO_INLINE")

package dream.utils

/**
 * Casts [A] to [B]
 */
inline fun <A, B> A.cast(): B = this as B

/**
 * Casts [A] to [B]
 */
inline fun <A, B> A.castOrNull(): B? = this as? B

/**
 * Executes [collector] and returns the value if no exceptions
 * when executing, otherwise return [default] value.
 */
inline fun <T : Any> catching(default: T, collector: () -> T): T {
  return try {
    collector()
  } catch (ex: Exception) {
    return default
  }
}

/**
 * Executes [collector] and returns the value if no exceptions
 * when executing, otherwise return [default] value.
 */
inline fun <T : Any> catchingOrNull(default: T? = null, collector: () -> T): T? {
  return try {
    collector()
  } catch (ex: Exception) {
    return default
  }
}

/**
 * Executes [collector] and do nothing if any exception is thrown
 */
inline fun <T : Any> catches(collector: () -> T) {
  try {
    collector()
  } catch (_: Exception) {

  }
}

/**
 * Prints [s] marked.
 */
fun printm(s: Any) {
  println()
  println(s)
  println()
}
