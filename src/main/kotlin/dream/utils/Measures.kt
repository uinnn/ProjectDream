package dream.utils

import kotlin.time.*

/**
 * Measures [block] execution time [repeat] times and print the results.
 */
fun measure(parts: Int = 1, repeat: Int = 1, block: () -> Unit) {
   val times = arrayListOf<Duration>()
   
   repeat(parts) {
      times += measureTime {
         repeat(repeat) {
            block()
         }
      }
   }
   
   times.forEachIndexed { index, duration ->
      println("#${index + 1} " + duration.toString(DurationUnit.MILLISECONDS, 6))
   }
   
   println("\nSum: ${times.sumOf { it.inWholeMilliseconds }} ms")
}
