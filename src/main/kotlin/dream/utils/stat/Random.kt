package dream.utils.stat

import dream.utils.list
import korlibs.datastructure.random.fastRandom

/**
 * Samples a single random element `T` from a `List<T>`, and throws an error if no elements exist
 */
fun <T> List<T>.randomFirst() = randomFirstOrNull() ?: throw NoSuchElementException("No elements found!")

/**
 * Samples a single random element `T` from a `List<T>`, and returns `null` if no elements exist
 */
fun <T> List<T>.randomFirstOrNull(): T? {
  if (isEmpty()) return null
  return fastRandom()
}

/**
 * Samples a single random element `T` from a `Sequence<T>`, and throws an error if no elements exist
 */
fun <T> Sequence<T>.randomFirst() = toList().randomFirst()

/**
 * Samples a single random element `T` from a `Sequence<T>`, and returns `null` if no elements exist
 */
fun <T> Sequence<T>.randomFirstOrNull() = toList().randomFirstOrNull()

/**
 * Samples a single random element `T` from a `Sequence<T>`, and throws an error if no elements exist
 */
fun <T> Iterable<T>.randomFirst() = toList().randomFirst()

/**
 * Samples a single random element `T` from an `Iterable<T>`, and returns `null` if no elements exist
 */
fun <T> Iterable<T>.randomFirstOrNull() = toList().randomFirstOrNull()

/**
 * Samples `n` distinct random elements `T` from a `Sequence<T>`
 */
fun <T> Sequence<T>.randomDistinct(sampleSize: Int) = toList().randomDistinct(sampleSize)

/**
 * Samples `n` distinct random elements `T` from an `Iterable<T>`
 */
fun <T> List<T>.randomDistinct(amount: Int): List<T> {
  return list(amount) { fastRandom() }.distinct()
}

/**
 * Samples `n` distinct random elements `T` from an `Iterable<T>`
 */
fun <T> Iterable<T>.randomDistinct(amount: Int): List<T> {
  if (this is List) return randomDistinct(amount)
  return toList().randomDistinct(amount)
}

/**
 * Samples `n` random elements `T` from a `Sequence<T>`
 */
fun <T> Sequence<T>.random(sampleSize: Int) = toList().random(sampleSize)

/**
 * Samples `n` random elements `T` from an `Iterable<T>`
 */
fun <T> List<T>.random(amount: Int): List<T> {
  return list(amount) { fastRandom() }
}

/**
 * Samples `n` random elements `T` from an `Iterable<T>`
 */
fun <T> Iterable<T>.random(amount: Int): List<T> {
  if (this is List) return random(amount)
  return toList().random(amount)
}

