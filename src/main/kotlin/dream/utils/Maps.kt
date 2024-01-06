package dream.utils

import com.google.common.collect.*
import dream.utils.stat.*
import java.util.*
import kotlin.reflect.*

typealias Entry<K, V> = Map.Entry<K, V>
typealias MutableEntry<K, V> = MutableMap.MutableEntry<K, V>

/**
 * Creates a new enum map by the given [K] Class.
 */
inline fun <reified K : Enum<K>, V> enumMap(): EnumMap<K, V> = EnumMap(K::class.java)
fun <K : Enum<K>, V> enumMap(clazz: KClass<K>): EnumMap<K, V> = EnumMap(clazz.java)

fun <K : Enum<K>> enumSet(clazz: KClass<K>): EnumSet<K> = EnumSet.allOf(clazz.java)
inline fun <reified K : Enum<K>> enumSet(): EnumSet<K> = EnumSet.allOf(K::class.java)

/**
 * Creates a new empty tree map with an optional [comparator].
 */
fun <K, V> treeMapOf(comparator: Comparator<K>? = null) = TreeMap<K, V>(comparator)

/**
 * Creates a new empty tree map with an optional [comparator].
 */
fun <K, V> treeMapOf(
  vararg elements: Pair<K, V>,
  comparator: Comparator<K>? = null,
) = TreeMap<K, V>(comparator).also { it.putAll(elements.toMap()) }

/**
 * Converts this map to a tree map.
 */
fun <K : Comparable<K>, V> Map<K, V>.toTreeMap() = TreeMap(this)

/**
 * Converts this map to a tree map with the specified [comparator].
 */
fun <K, V> Map<K, V>.toTreeMap(comparator: Comparator<K>) = TreeMap<K, V>(comparator).also { it.putAll(this) }

fun <K, V> Map<K, V>.first() = entries.first()
fun <K, V> Map<K, V>.firstOrNull() = entries.firstOrNull()

inline fun <K, V> Map<K, V>.first(predicate: (Map.Entry<K, V>) -> Boolean) = entries.first(predicate)
inline fun <K, V> Map<K, V>.firstOrNull(predicate: (Entry<K, V>) -> Boolean) = entries.firstOrNull(predicate)

fun <K, V> Map<K, V>.last() = entries.last()
fun <K, V> Map<K, V>.lastOrNull() = entries.lastOrNull()

inline fun <K, V> Map<K, V>.last(predicate: (Entry<K, V>) -> Boolean) = entries.last(predicate)
inline fun <K, V> Map<K, V>.lastOrNull(predicate: (Entry<K, V>) -> Boolean) = entries.lastOrNull(predicate)

fun <K, V> Map<K, V>.random() = entries.random()
fun <K, V> Map<K, V>.randomOrNull() = entries.randomOrNull()

infix fun <K, V> Map<K, V>.random(amount: Int) = entries.random(amount)
infix fun <K, V> Map<K, V>.randomDistinct(amount: Int) = entries.randomDistinct(amount)

infix fun <K, V> Map<K, V>.take(amount: Int) = entries.take(amount)
infix fun <K, V> Map<K, V>.drop(amount: Int) = entries.drop(amount)

fun <K, V> Map<K, V>.firstValue() = values.first()
fun <K, V> Map<K, V>.firstOrNullValue() = values.firstOrNull()

inline fun <K, V> Map<K, V>.firstOrNullValue(predicate: (V) -> Boolean) = values.firstOrNull(predicate)

fun <K, V> Map<K, V>.lastValue() = values.last()
fun <K, V> Map<K, V>.lastOrNullValue() = values.lastOrNull()

inline fun <K, V> Map<K, V>.lastOrNullValue(predicate: (V) -> Boolean) = values.lastOrNull(predicate)

fun <K, V> Map<K, V>.randomValue() = values.random()
fun <K, V> Map<K, V>.randomOrNullValue() = values.randomOrNull()

infix fun <K, V> Map<K, V>.randomValue(amount: Int) = values.random(amount)
infix fun <K, V> Map<K, V>.randomDistinctValue(amount: Int) = values.randomDistinct(amount)

infix fun <K, V> Map<K, V>.takeValue(amount: Int) = values.take(amount)
infix fun <K, V> Map<K, V>.dropValue(amount: Int) = values.drop(amount)

fun <K, V> Map<K, V>.firstKey() = keys.first()
fun <K, V> Map<K, V>.firstOrNullKey() = keys.firstOrNull()

inline fun <K, V> Map<K, V>.firstOrNullKey(predicate: (K) -> Boolean) = keys.firstOrNull(predicate)

fun <K, V> Map<K, V>.lastKey() = keys.last()
fun <K, V> Map<K, V>.lastOrNullKey() = keys.lastOrNull()

inline fun <K, V> Map<K, V>.lastOrNullKey(predicate: (K) -> Boolean) = keys.lastOrNull(predicate)

fun <K, V> Map<K, V>.randomKey() = keys.random()
fun <K, V> Map<K, V>.randomOrNullKey() = keys.randomOrNull()

infix fun <K, V> Map<K, V>.randomKey(amount: Int) = keys.random(amount)
infix fun <K, V> Map<K, V>.randomDistinctKey(amount: Int) = keys.randomDistinct(amount)

infix fun <K, V> Map<K, V>.takeKey(amount: Int) = keys.take(amount)
infix fun <K, V> Map<K, V>.dropKey(amount: Int) = keys.drop(amount)

/**
 * Finds the first value occurrence by the given [predicate] or nulls if nothing is found.
 */
inline fun <K, V> Map<K, V>.findValue(predicate: (V) -> Boolean): V? {
  return values.find(predicate)
}

/**
 * Finds the last value occurrence by the given [predicate] or nulls if nothing is found.
 */
inline fun <K, V> Map<K, V>.findLastValue(predicate: (V) -> Boolean): V? {
  return values.findLast(predicate)
}

/**
 * Finds the first key occurrence by the given [predicate] or nulls if nothing is found.
 */
inline fun <K, V> Map<K, V>.findKey(predicate: (K) -> Boolean): K? {
  return keys.find(predicate)
}

/**
 * Finds the last key occurrence by the given [predicate] or nulls if nothing is found.
 */
inline fun <K, V> Map<K, V>.findLastKey(predicate: (K) -> Boolean): K? {
  return keys.findLast(predicate)
}

/**
 * Finds the first value occurrence by the given [predicate]
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstValue(predicate: (V) -> Boolean): V {
  return values.first(predicate)
}

/**
 * Finds the last value occurrence by the given [predicate]
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastValue(predicate: (V) -> Boolean): V {
  return values.last(predicate)
}

/**
 * Finds the first value occurrence of the given [predicate] selecting by entries
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstValueBy(predicate: (Entry<K, V>) -> Boolean): V {
  for (entry in this) {
    if (predicate(entry))
      return entry.value
  }
  error("No value found with the given predicate.")
}

/**
 * Finds the last value occurrence of the given [predicate] selecting by entries
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastValueBy(predicate: (Entry<K, V>) -> Boolean): V {
  var last: V? = null
  var found = false
  for (entry in this) {
    if (predicate(entry)) {
      last = entry.value
      found = true
    }
  }
  if (!found) error("No value found with the given predicate.")
  return last.cast()
}

/**
 * Finds the first value occurrence of the given [predicate] selecting by keys
 * or throws an exception if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstValueByKey(predicate: (K) -> Boolean): V {
  for ((key, value) in this) {
    if (predicate(key))
      return value
  }
  error("No value found with the given predicate.")
}

/**
 * Finds the last value occurrence of the given [predicate] selecting by keys
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastValueByKey(predicate: (K) -> Boolean): V {
  var last: V? = null
  var found = false
  for ((key, value) in this) {
    if (predicate(key)) {
      last = value
      found = true
    }
  }
  if (!found) error("No value found with the given predicate.")
  return last.cast()
}

/**
 * Finds the first key occurrence of the given [predicate]
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstKey(predicate: (K) -> Boolean): K {
  return keys.first(predicate)
}

/**
 * Finds the last key occurrence of the given [predicate]
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastKey(predicate: (K) -> Boolean): K {
  return keys.last(predicate)
}

/**
 * Finds the first key occurrence of the given [predicate] selecting by entries
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstKeyBy(predicate: (Entry<K, V>) -> Boolean): K {
  for (entry in this) {
    if (predicate(entry))
      return entry.key
  }
  error("No key found with the given predicate.")
}

/**
 * Finds the last key occurrence of the given [predicate] selecting by entries
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastKeyBy(predicate: (Entry<K, V>) -> Boolean): K {
  var last: K? = null
  var found = false
  for (entry in this) {
    if (predicate(entry)) {
      last = entry.key
      found = true
    }
  }
  if (!found) error("No key found with the given predicate.")
  return last.cast()
}

/**
 * Finds the first key occurrence of the given [predicate] selecting by values
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.firstKeyByValue(predicate: (V) -> Boolean): K {
  for ((key, value) in this) {
    if (predicate(value))
      return key
  }
  error("No key found with the given predicate.")
}

/**
 * Finds the last key occurrence of the given [predicate] selecting by values
 * or throws a [NoSuchElementException] if nothing is found.
 */
inline fun <K, V> Map<K, V>.lastKeyByValue(predicate: (V) -> Boolean): K {
  var last: K? = null
  var found = false
  for ((key, value) in this) {
    if (predicate(value)) {
      last = key
      found = true
    }
  }
  if (!found) error("No key found with the given predicate.")
  return last.cast()
}

/**
 * Performs the given [action] for each key of this map.
 */
inline fun <K, V> Map<K, V>.forEachKey(action: (K) -> Unit) = keys.forEach(action)

/**
 * Performs the given [action] for each value of this map.
 */
inline fun <K, V> Map<K, V>.forEachValue(action: (V) -> Unit) = values.forEach(action)

/**
 * Performs the given [action] for each key indexed of this map.
 */
inline fun <K, V> Map<K, V>.forEachKeyIndexed(action: (Int, K) -> Unit) = keys.forEachIndexed(action)

/**
 * Performs the given [action] for each value indexed of this map.
 */
inline fun <K, V> Map<K, V>.forEachValueIndexed(action: (Int, V) -> Unit) = values.forEachIndexed(action)

/**
 * Performs the given [action] on each key of this map.
 */
inline fun <K, V> Map<K, V>.onEachKey(action: (K) -> Unit) = keys.onEach(action)

/**
 * Performs the given [action] on each value of this map.
 */
inline fun <K, V> Map<K, V>.onEachValue(action: (V) -> Unit) = values.onEach(action)

/**
 * Performs the given [action] on each key indexed of this map.
 */
inline fun <K, V> Map<K, V>.onEachKeyIndexed(action: (Int, K) -> Unit) = keys.onEachIndexed(action)

/**
 * Performs the given [action] on each value indexed of this map.
 */
inline fun <K, V> Map<K, V>.onEachValueIndexed(action: (Int, V) -> Unit) = values.onEachIndexed(action)

/**
 * Create a sorted map of this map by natural comparator of [K].
 */
fun <K : Comparable<K>, V> Map<out K, V>.sorted() = TreeMap(this)

/**
 * Create a sorted map of this map by the comparator of the comparable [K] in descending order.
 */
fun <K : Comparable<K>, V> Map<K, V>.sortedDescending() = toTreeMap(compareByDescending { it })

/**
 * Create a sorted map of this map by the given comparator comparing the keys of this map.
 */
fun <K, V> Map<K, V>.sortedWith(comparator: Comparator<K>) = toTreeMap(comparator)

/**
 * Create a sorted map of this map by the given comparator comparing the keys of this map.
 */
inline fun <K, V> Map<K, V>.sortedBy(crossinline compare: (K) -> Boolean) =
  toTreeMap(compareBy(compare))

/**
 * Create a sorted map of this map by the given comparator comparing the keys of this map in desceding order.
 */
fun <K, V> Map<K, V>.sortedWithDescending(comparator: Comparator<K>) =
  toTreeMap(comparator.reversed())

/**
 * Create a sorted map of this map by the given comparator comparing the keys of this map in desceding order.
 */
inline fun <K, V> Map<K, V>.sortedByDescending(crossinline compare: (K) -> Boolean) =
  toTreeMap(compareByDescending(compare))

/**
 * Gets the key associated with [value] in this [BiMap].
 */
operator fun <K : Any, V : Any> BiMap<K, V>.get(value: V): K? = inverse()[value]

/**
 * Populates the given [destination] with [keys] and [values]
 */
fun <K, V, M : MutableMap<K, V>> populateMap(destination: M, keys: Iterable<K>, values: Iterable<V>): M {
  val iterator = values.iterator()
  for (key in keys) {
    destination[key] = iterator.next()
  }

  return destination
}

/**
 * Populates a [LinkedHashMap] with [keys] and [values]
 */
fun <K, V> populateMap(keys: Iterable<K>, values: Iterable<V>): LinkedHashMap<K, V> {
  return populateMap(LinkedHashMap(), keys, values)
}
