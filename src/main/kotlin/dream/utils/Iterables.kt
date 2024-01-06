package dream.utils

import com.soywiz.kds.random.*

/**
 * Returns the size of this iterable.
 */
val Iterable<*>.size get() = count()
fun Iterable<*>.isEmpty() = size == 0
fun Iterable<*>.isNotEmpty() = size > 0

fun <T> Iterable<T>.asSequence() = sequence<T> { iterator() }

/**
 * Returns the cartesian product of this list.
 */
fun <T> Iterable<Iterable<T>>.cartesian(): Set<List<T>> {
  if (isEmpty()) return emptySet()
  return drop(1).fold(
    first()
      .map(::listOf)
  ) { acc, iterable -> acc.flatMap { list -> iterable.map(list::plus) } }
    .toSet()
}

/**
 * Shuffles this iterable using FastRandom.
 */
fun <T> Iterable<T>.fastShuffled() = shuffled(FastRandom)

/**
 * Calculates the max result from [initial] using [calculator].
 *
 * This is similar to [sumOf] but this not ``sum (+=)`` and yes ``set (=)``
 */
fun <T> Iterable<T>.calculate(initial: Int = 0, calculator: (T, Int) -> Int): Int {
  var init = initial
  for (value in this) {
    init = calculator(value, init)
  }
  return init
}

/**
 * Calculates the max result from [initial] using [calculator].
 *
 * This is similar to [sumOf] but this not ``sum (+=)`` and yes ``set (=)``
 */
fun <T> Array<out T>.calculate(initial: Int = 0, calculator: (T, Int) -> Int): Int {
  var init = initial
  for (value in this) {
    init = calculator(value, init)
  }
  return init
}

/**
 * A similar function to [associateTo], but this accepts nullable [transform]
 * and if returns null, the value will not be associated on the map.
 */
inline fun <T, K, V, M : MutableMap<in K, in V>> Iterable<T>.associateToNotNull(
  destination: M,
  transform: (T) -> Pair<K, V>?,
): M {
  for (element in this) {
    val data = transform(element) ?: continue
    destination += data
  }
  return destination
}

/**
 * Cycles this iterable to the next value of [value]
 */
fun <T> Iterable<T>.next(value: T): T {
  val iterator = iterator()
  while (iterator.hasNext()) {
    val next = iterator.next()
    if (next == value) {
      return if (iterator.hasNext()) next else iterator().next()
    }
  }

  return iterator.next()
}

/**
 * Maps this iterable to a mutable list.
 */
fun <T, E> Iterable<T>.mapMutable(selector: (T) -> E): MutableList<E> = mapTo(ArrayList(size), selector)

/**
 * Calls [action] for every element at this iterable synchronized with [lock].
 */
inline fun <T> Iterable<T>.forEachSync(lock: Any = this, action: (T) -> Unit) {
  synchronized(lock) {
    forEach(action)
  }
}

/**
 * Finds the first element that is [R].
 */
inline fun <reified R> Iterable<*>.findIs(): R? {
  for (element in this) if (element is R) return element
  return null
}

/**
 * Filter to [destination] all elements that is not null.
 *
 * Working like a filter-map.
 */
inline fun <R, T, C : MutableCollection<in R>> Iterable<T>.filterNotNullTo(
  destination: C,
  selector: (T) -> R?,
): C {
  for (element in this) {
    val selected = selector(element)
    if (selected != null) {
      destination.add(selected)
    }
  }
  return destination
}

/**
 * Filter to an list all elements that is not null.
 *
 * Working like a filter-map.
 */
inline fun <R, T> Iterable<T>.filterNotNull(selector: (T) -> R?): List<R> {
  return filterNotNullTo(ArrayList(), selector)
}

/**
 * Filter to [destination] all elements that is [R].
 *
 * Working like a filter-map.
 */
inline fun <reified R, T, C : MutableCollection<in R>> Iterable<T>.filterIsInstanceTo(
  destination: C,
  selector: (T) -> Any?,
): C {
  for (element in this) {
    val selected = selector(element)
    if (selected is R) {
      destination.add(selected)
    }
  }
  return destination
}

/**
 * Filter to an list all elements that is [R].
 *
 * Working like a filter-map.
 */
inline fun <reified R, T> Iterable<T>.filterIsInstance(selector: (T) -> Any?): List<R> {
  return filterIsInstanceTo(ArrayList(), selector)
}
