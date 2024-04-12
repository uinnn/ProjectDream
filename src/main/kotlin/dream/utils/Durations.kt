package dream.utils

import korlibs.time.DateTimeSpan
import korlibs.time.MonthSpan
import korlibs.time.TimeSpan
import org.joda.time.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

inline val Int.weeks get() = (this * 7).days
inline val Long.weeks get() = (this * 7).days

inline val Int.months get() = (this * 30).days
inline val Long.months get() = (this * 30).days

inline val Int.years get() = (this * 365).days
inline val Long.years get() = (this * 365).days

fun Duration.toTimeSpan() = TimeSpan(inWholeMilliseconds.toDouble())
fun Duration.toMonthSpan() = MonthSpan((inWholeDays * 30).toInt())
fun Duration.toDateTimeSpan() = DateTimeSpan(toMonthSpan(), toTimeSpan())

fun Duration.toJodaDuration() = Duration(inWholeMilliseconds)
fun Duration.toPeriod() = Period(inWholeMilliseconds)
