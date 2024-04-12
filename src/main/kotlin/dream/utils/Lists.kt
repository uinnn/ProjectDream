package dream.utils

import korlibs.datastructure.random.FastRandom
import java.util.*

/**
 * Creates a synchronized list from this list.
 */
fun <T> MutableList<T>.sync(): MutableList<T> = Collections.synchronizedList(this)

/**
 * Swaps a index to another index.
 */
fun <T> MutableList<T>.swap(from: Int, to: Int) = Collections.swap(this, from, to)

/**
 * Shuffles this list using FastRandom.
 */
fun <T> MutableList<T>.fastShuffle() = shuffle(FastRandom)

/**
 * Maps [range] with [transform].
 */
inline fun <T> list(range: IntRange, transform: (Int) -> T): List<T> {
  return range.map(transform)
}

/**
 * Maps a range of `start..max` with [transform].
 */
inline fun <T> list(start: Int, max: Int, transform: (Int) -> T): List<T> {
  return (start..max).map(transform)
}

/**
 * Maps a range of `0..max` with [transform].
 */
inline fun <T> list(max: Int, transform: (Int) -> T): List<T> {
  return (0..max).map(transform)
}

inline fun <T> MutableList<T>.getOrPut(index: Int, value: () -> T): T {
  val present = getOrNull(index)
  if (present != null)
    return present

  val putted = value.invoke()
  set(index, putted)
  return putted
}

inline fun <T> MutableList<T>.getOrAdd(index: Int, value: () -> T): T {
  val present = getOrNull(index)
  if (present != null)
    return present

  val putted = value.invoke()
  add(putted)
  return putted
}
