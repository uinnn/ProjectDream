package dream.misc

import kotlin.time.*

/**
 * A progression for duration.
 */
class DurationProgression(
  val start: Duration,
  val last: Duration,
  val step: Duration,
) : Iterable<Duration>, Iterator<Duration> {
  
  override fun iterator() = this
  
  var hasNext = start < last
  var next = if (hasNext) start else last
  
  override fun hasNext() = hasNext
  
  override fun next(): Duration {
    val value = next
    if (value >= last) {
      if (!hasNext) throw NoSuchElementException()
      hasNext = false
    } else {
      next += step
    }
    return value
  }
  
}


infix fun ClosedRange<Duration>.step(step: Duration) = DurationProgression(start, endInclusive, step)
