package dream.utils.stat

import dream.utils.stat.range.*
import org.apache.commons.math3.stat.*
import org.apache.commons.math3.stat.descriptive.*

val LongArray.descriptiveStatistics: Descriptives
   get() = DescriptiveStatistics().apply { forEach { addValue(it.toDouble()) } }.let(::ApacheDescriptives)

fun LongArray.geometricMean() = StatUtils.geometricMean(asSequence().map { it.toDouble() }.toList().toDoubleArray())
fun LongArray.median() = percentile(50.0)
fun LongArray.percentile(percentile: Double) = StatUtils.percentile(asSequence()
   .map { it.toDouble() }
   .toList()
   .toDoubleArray(), percentile)

fun LongArray.variance() = StatUtils.variance(asSequence().map { it.toDouble() }.toList().toDoubleArray())
fun LongArray.sumOfSquares() = StatUtils.sumSq(asSequence().map { it.toDouble() }.toList().toDoubleArray())
fun LongArray.standardDeviation() = descriptiveStatistics.standardDeviation
fun LongArray.normalize() = StatUtils.normalize(asSequence().map { it.toDouble() }.toList().toDoubleArray())
val LongArray.kurtosis get() = descriptiveStatistics.kurtosis
val LongArray.skewness get() = descriptiveStatistics.skewness


// AGGREGATION OPERATORS

inline fun <T, K> Sequence<T>.sumBy(
   crossinline keySelector: (T) -> K, crossinline longSelector: (T) -> Long,
) = groupApply(keySelector, longSelector) { it.sum() }

inline fun <T, K> Iterable<T>.sumBy(
   crossinline keySelector: (T) -> K, crossinline longSelector: (T) -> Long,
) = asSequence().sumBy(keySelector, longSelector)


fun <K> Sequence<Pair<K, Long>>.sumBy() = groupApply({ it.first }, { it.second }) { it.sum() }

fun <K> Iterable<Pair<K, Long>>.sumBy() = asSequence().sumBy()


inline fun <T, K> Sequence<T>.averageBy(
   crossinline keySelector: (T) -> K, crossinline longSelector: (T) -> Long,
) = groupApply(keySelector, longSelector) { it.average() }

inline fun <T, K> Iterable<T>.averageBy(
   crossinline keySelector: (T) -> K, crossinline valueSelector: (T) -> Long,
) = asSequence().averageBy(keySelector, valueSelector)


fun <K> Sequence<Pair<K, Long>>.averageBy() = groupApply({ it.first }, { it.second }) { it.average() }

fun <K> Iterable<Pair<K, Long>>.averageBy() = asSequence().averageBy()


fun Sequence<Long>.longRange() = toList().longRange()
fun Iterable<Long>.longRange() = toList().let {
   (it.minOrNull() ?: throw Exception("At least one element must be present"))..(it.maxOrNull() ?: throw Exception(
      "At least one element must be present"
   ))
}

inline fun <T, K> Sequence<T>.longRangeBy(
   crossinline keySelector: (T) -> K, crossinline longSelector: (T) -> Long,
) = groupApply(keySelector, longSelector) { it.range() }

inline fun <T, K> Iterable<T>.longRangeBy(
   crossinline keySelector: (T) -> K, crossinline longSelector: (T) -> Long,
) = asSequence().rangeBy(keySelector, longSelector)


// bin operators

inline fun <T> Sequence<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, rangeStart: Long? = null,
) = toList().binByLong(binSize, valueSelector, rangeStart)

inline fun <T, G> Sequence<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, crossinline groupOp: (List<T>) -> G,
   rangeStart: Long? = null,
) = toList().binByLong(binSize, valueSelector, groupOp, rangeStart)


inline fun <T> Iterable<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, rangeStart: Long? = null,
): BinModel<List<T>, Long> = toList().binByLong(binSize, valueSelector, { it }, rangeStart)


inline fun <T, G> Iterable<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, crossinline groupOp: (List<T>) -> G,
   rangeStart: Long? = null,
) = toList().binByLong(binSize, valueSelector, groupOp, rangeStart)


inline fun <T> List<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, rangeStart: Long? = null,
): BinModel<List<T>, Long> = binByLong(binSize, valueSelector, { it }, rangeStart)

inline fun <T, G> List<T>.binByLong(
   binSize: Long, crossinline valueSelector: (T) -> Long, crossinline groupOp: (List<T>) -> G,
   rangeStart: Long? = null,
): BinModel<G, Long> {
   
   val groupedByC = asSequence().groupBy(valueSelector)
   val minC = rangeStart ?: groupedByC.keys.minOrNull()!!
   val maxC = groupedByC.keys.maxOrNull()!!
   
   val bins = mutableListOf<XClosedRange<Long>>().apply {
      var currentRangeStart = minC
      var currentRangeEnd = minC
      while (currentRangeEnd < maxC) {
         currentRangeEnd = currentRangeStart + binSize - 1L
         add(XClosedRange(currentRangeStart, currentRangeEnd))
         currentRangeStart = currentRangeEnd + 1L
      }
   }
   
   return bins.asSequence().map { it to mutableListOf<T>() }.map { binWithList ->
      groupedByC.entries
         .asSequence()
         .filter { it.key in binWithList.first }
         .forEach { binWithList.second.addAll(it.value) }
      binWithList
   }.map { Bin(it.first, groupOp(it.second)) }.toList().let(::BinModel)
}

fun <T, C : Comparable<C>> BinModel<List<T>, C>.sumByLong(selector: (T) -> Long): BinModel<Long, C> =
   BinModel(bins.map {
      Bin(
         it.range, it.value.map(selector).sum()
      )
   })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.averageByLong(selector: (T) -> Long): BinModel<Double, C> = BinModel(
   bins.map { Bin(it.range, it.value.map(selector).average()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.doubleRangeBy(selector: (T) -> Long): BinModel<LongRange, C> = BinModel(
   bins.map { Bin(it.range, it.value.map(selector).longRange()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.geometricMeanByLong(selector: (T) -> Long): BinModel<Double, C> =
   BinModel(
      bins.map { Bin(it.range, it.value.map(selector).geometricMean()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.medianByLong(selector: (T) -> Long): BinModel<Double, C> =
   BinModel(bins.map {
      Bin(
         it.range, it.value.map(selector).median()
      )
   })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.percentileByLong(
   selector: (T) -> Long, percentile: Double,
): BinModel<Double, C> = BinModel(bins.map { Bin(it.range, it.value.map(selector).percentile(percentile)) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.varianceByLong(selector: (T) -> Long): BinModel<Double, C> = BinModel(
   bins.map { Bin(it.range, it.value.map(selector).variance()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.sumOfSquaresByLong(selector: (T) -> Long): BinModel<Double, C> =
   BinModel(
      bins.map { Bin(it.range, it.value.map(selector).sumOfSquares()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.normalizeByLong(selector: (T) -> Long): BinModel<DoubleArray, C> =
   BinModel(
      bins.map { Bin(it.range, it.value.map(selector).normalize()) })

fun <T, C : Comparable<C>> BinModel<List<T>, C>.descriptiveStatisticsByLong(
   selector: (T) -> Long,
): BinModel<Descriptives, C> = BinModel(
   bins.map { Bin(it.range, it.value.map(selector).descriptiveStatistics) })


fun <K> Map<K, List<Long>>.sum(): Map<K, Long> = entries.associate { it.key to it.value.sum() }
fun <K> Map<K, List<Long>>.average(): Map<K, Double> = entries.associate { it.key to it.value.average() }
fun <K> Map<K, List<Long>>.longRange(): Map<K, Iterable<Long>> = entries.associate { it.key to it.value.longRange() }
fun <K> Map<K, List<Long>>.geometricMean(): Map<K, Double> = entries.associate { it.key to it.value.geometricMean() }
fun <K> Map<K, List<Long>>.median(): Map<K, Double> = entries.associate { it.key to it.value.median() }
fun <K> Map<K, List<Long>>.percentile(percentile: Double): Map<K, Double> = entries.associate {
   it.key to it.value.percentile(
      percentile
   )
}

fun <K> Map<K, List<Long>>.variance(): Map<K, Double> = entries.associate { it.key to it.value.variance() }
fun <K> Map<K, List<Long>>.sumOfSquares(): Map<K, Double> = entries.associate { it.key to it.value.sumOfSquares() }
fun <K> Map<K, List<Long>>.normalize(): Map<K, DoubleArray> = entries.associate { it.key to it.value.normalize() }
fun <K> Map<K, List<Long>>.descriptiveStatistics(): Map<K, Descriptives> =
   entries.associate { it.key to it.value.descriptiveStatistics }

fun <K, V> Map<K, List<V>>.sumByLong(selector: (V) -> Long): Map<K, Long> = entries.associate {
   it.key to it.value.map(selector).sum()
}

fun <K, V> Map<K, List<V>>.averageByLong(selector: (V) -> Long): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).average()
}

fun <K, V> Map<K, List<V>>.LongRangeBy(selector: (V) -> Long): Map<K, Iterable<Long>> = entries.associate {
   it.key to it.value.map(selector).longRange()
}

fun <K, V> Map<K, List<V>>.geometricMeanByLong(selector: (V) -> Long): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).geometricMean()
}

fun <K, V> Map<K, List<V>>.medianByLong(selector: (V) -> Long): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).median()
}

fun <K, V> Map<K, List<V>>.percentileByLong(
   selector: (V) -> Long, percentile: Double,
): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).percentile(percentile)
}

fun <K, V> Map<K, List<V>>.varianceByLong(selector: (V) -> Long): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).variance()
}

fun <K, V> Map<K, List<V>>.sumOfSquaresByLong(selector: (V) -> Long): Map<K, Double> = entries.associate {
   it.key to it.value.map(selector).sumOfSquares()
}

fun <K, V> Map<K, List<V>>.normalizeByLong(selector: (V) -> Long): Map<K, DoubleArray> = entries.associate {
   it.key to it.value.map(selector).normalize()
}

fun <K, V> Map<K, List<V>>.descriptiveStatisticsByLong(selector: (V) -> Long): Map<K, Descriptives> =
   entries.associate {
      it.key to it.value.map(
         selector
      ).descriptiveStatistics
   }
