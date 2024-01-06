package dream.utils.stat

inline fun <T, R : Comparable<R>, K> Sequence<T>.minBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = groupApply(keySelector, valueSelector) { it.minOrNull() }

inline fun <T, R : Comparable<R>, K> Iterable<T>.minBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = asSequence().minBy(keySelector, valueSelector)

fun <K, R : Comparable<R>> Sequence<Pair<K, R>>.minBy() = groupApply({ it.first }, { it.second }) { it.minOrNull() }

fun <K, R : Comparable<R>> Iterable<Pair<K, R>>.minBy() = asSequence().minBy()


inline fun <T, R : Comparable<R>, K> Sequence<T>.maxBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = groupApply(keySelector, valueSelector) { it.maxOrNull() }

inline fun <T, R : Comparable<R>, K> Iterable<T>.maxBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = asSequence().maxBy(keySelector, valueSelector)


fun <T : Comparable<T>, K> Sequence<Pair<K, T>>.maxBy() = groupApply({ it.first }, { it.second }) { it.maxOrNull() }

fun <T : Comparable<T>, K> Iterable<Pair<K, T>>.maxBy() = asSequence().maxBy()


fun <T : Comparable<T>> Sequence<T>.range() = toList().range()
fun <T : Comparable<T>> Iterable<T>.range() = toList().let {
  (it.minOrNull() ?: throw Exception("At least one element must be present"))..(it.maxOrNull() ?: throw Exception(
    "At least one element must be present"
  ))
}

inline fun <T, R : Comparable<R>, K> Sequence<T>.rangeBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = groupApply(keySelector, valueSelector) { it.range() }

inline fun <T, R : Comparable<R>, K> Iterable<T>.rangeBy(
  crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> R,
) = asSequence().rangeBy(keySelector, valueSelector)
