package dream.utils.stat

import org.apache.commons.math3.ml.clustering.*

fun Collection<Pair<Double, Double>>.kMeansCluster(k: Int, maxIterations: Int) = kMeansCluster(k,
   maxIterations,
   { it.first },
   { it.second })

fun Sequence<Pair<Double, Double>>.kMeansCluster(k: Int, maxIterations: Int) = toList().kMeansCluster(k,
   maxIterations,
   { it.first },
   { it.second })

inline fun <T> Collection<T>.kMeansCluster(
   k: Int, maxIterations: Int, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = asSequence().map { ClusterInput(it, doubleArrayOf(xSelector(it), ySelector(it))) }.toList().let { input ->
   KMeansPlusPlusClusterer<ClusterInput<T>>(k, maxIterations).cluster(input).map { cluster ->
      Centroid((cluster.center).point.let { DoublePoint(it[0], it[1]) }, cluster.points.map { it.item })
   }
}


inline fun <T> Sequence<T>.kMeansCluster(
   k: Int, maxIterations: Int, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = toList().kMeansCluster(k, maxIterations, xSelector, ySelector)


inline fun <T> Collection<T>.fuzzyKMeansCluster(
   k: Int, fuzziness: Double, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = asSequence().map { ClusterInput(it, doubleArrayOf(xSelector(it), ySelector(it))) }.toList().let { inputs ->
   FuzzyKMeansClusterer<ClusterInput<T>>(k, fuzziness).cluster(inputs).map { cluster ->
      Centroid((cluster.center).point.let { DoublePoint(it[0], it[1]) }, cluster.points.map { it.item })
   }
}

inline fun <T> Sequence<T>.fuzzyKMeansCluster(
   k: Int, fuzziness: Double, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = toList().fuzzyKMeansCluster(k, fuzziness, xSelector, ySelector)

fun Collection<Pair<Double, Double>>.multiKMeansCluster(
   k: Int, maxIterations: Int, trialCount: Int,
) = multiKMeansCluster(k, maxIterations, trialCount, { it.first }, { it.second })

fun Sequence<Pair<Double, Double>>.multiKMeansCluster(
   k: Int, maxIterations: Int, trialCount: Int,
) = toList().multiKMeansCluster(k, maxIterations, trialCount, { it.first }, { it.second })

inline fun <T> Sequence<T>.multiKMeansCluster(
   k: Int,
   maxIterations: Int,
   trialCount: Int,
   crossinline xSelector: (T) -> Double,
   crossinline ySelector: (T) -> Double,
) = toList().multiKMeansCluster(k, maxIterations, trialCount, xSelector, ySelector)


inline fun <T> Collection<T>.multiKMeansCluster(
   k: Int,
   maxIterations: Int,
   trialCount: Int,
   crossinline xSelector: (T) -> Double,
   crossinline ySelector: (T) -> Double,
) = asSequence().map { ClusterInput(it, doubleArrayOf(xSelector(it), ySelector(it))) }.toList().let { list ->
   KMeansPlusPlusClusterer<ClusterInput<T>>(k, maxIterations).let { clusterer ->
      MultiKMeansPlusPlusClusterer(clusterer, trialCount).cluster(list).map { cluster ->
         Centroid(DoublePoint(-1.0, -1.0), cluster.points.map { it.item })
      }
   }
}


inline fun <T> Collection<T>.dbScanCluster(
   maximumRadius: Double, minPoints: Int, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = asSequence().map { ClusterInput(it, doubleArrayOf(xSelector(it), ySelector(it))) }.toList().let { inputs ->
   DBSCANClusterer<ClusterInput<T>>(maximumRadius, minPoints).cluster(inputs).map { cluster ->
      Centroid(DoublePoint(-1.0, -1.0), cluster.points.map { it.item })
   }
}


inline fun <T> Sequence<T>.dbScanCluster(
   maximumRadius: Double, minPoints: Int, crossinline xSelector: (T) -> Double, crossinline ySelector: (T) -> Double,
) = toList().dbScanCluster(maximumRadius, minPoints, xSelector, ySelector)

class ClusterInput<out T>(val item: T, val location: DoubleArray) : Clusterable {
   override fun getPoint() = location
}

data class DoublePoint(val x: Double, val y: Double)
data class Centroid<out T>(val center: DoublePoint, val points: List<T>)
