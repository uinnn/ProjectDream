package dream.misc

import dream.utils.*
import org.joda.time.*

/**
 * Represents a date progression.
 */
class DateProgression(
  val start: LocalDateTime,
  val last: LocalDateTime,
  val step: kotlin.time.Duration,
) : Iterable<LocalDateTime>, Iterator<LocalDateTime> {
  
  override fun iterator() = this
  
  val duration: Duration = step.toJodaDuration()
  
  var hasNext = start < last
  var next = if (hasNext) start else last
  
  override fun hasNext() = hasNext
  
  override fun next(): LocalDateTime {
    val value = next
    if (value >= last) {
      if (!hasNext) throw NoSuchElementException()
      hasNext = false
    } else {
      next += duration
    }
    return value
  }
  
}

infix fun ClosedRange<LocalDateTime>.step(step: kotlin.time.Duration) = DateProgression(start, endInclusive, step)
