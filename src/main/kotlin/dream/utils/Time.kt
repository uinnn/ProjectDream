package dream.utils

import org.joda.time.*
import org.joda.time.format.*
import java.util.*
import kotlin.time.Duration

/**
 * Builds a new [DateTimeFormat].
 */
inline fun timeFormat(builder: DateTimeFormatterBuilder.() -> Unit): DateTimeFormatter {
  return DateTimeFormatterBuilder().apply(builder).toFormatter()
}

/**
 * Builds a new [DateTimeParser].
 */
inline fun timeParser(builder: DateTimeFormatterBuilder.() -> Unit): DateTimeParser {
  return DateTimeFormatterBuilder().apply(builder).toParser()
}

/**
 * Represents the default time formatter
 *
 * Example of format:
 * `
 * "01/01/2020 15:30:25"
 * `
 */
val TIME_FORMAT = timeFormat {
  appendDayOfMonth(2)
  appendLiteral('/')
  appendMonthOfYear(2)
  appendLiteral('/')
  appendYear(2, 4)
  appendLiteral(' ')
  appendHourOfDay(2)
  appendLiteral(':')
  appendMinuteOfHour(2)
  appendLiteral(':')
  appendSecondOfMinute(2)
}

/**
 * Represents the default time parser
 *
 * Examples of parsers:
 * `
 * "01/01/2020 15:30:25"
 * "01/01/2020 15:30"
 * "01/01/2020 15"
 * "01/01/2020"
 * `
 */
val TIME_PARSER = timeFormat {
  appendDayOfMonth(2)
  appendLiteral('/')
  appendMonthOfYear(2)
  appendLiteral('/')
  appendYear(2, 4)
  appendOptional(
    timeParser {
      appendLiteral(' ')
      appendHourOfDay(2)
      appendOptional(
        timeParser {
          appendLiteral(':')
          appendMinuteOfHour(2)
          appendOptional(
            timeParser {
              appendLiteral(':')
              appendSecondOfMinute(2)
            }
          )
        }
      )
    }
  )
}

/**
 * Gets the current time millis.
 */
inline val actualMillis: Long get() = System.currentTimeMillis()
inline val now get() = System.currentTimeMillis()

/**
 * Gets the current date time.
 */
val actualDate: LocalDateTime get() = LocalDateTime()

/**
 * Gets a date time based on this number (milliseconds).
 */
fun Number.toDate() = DateTime(toLong())
fun Number.toLocalDate() = LocalDate(toLong())
fun Number.toLocalDateTime() = LocalDateTime(toLong())

/**
 * Gets a date parsing this string.
 */
fun String.toDate(format: TimeFormat = TIME_PARSER): DateTime = format.parseDateTime(this)
fun String.toLocalDate(format: TimeFormat = TIME_PARSER): LocalDate = format.parseLocalDate(this)
fun String.toLocalDateTime(format: TimeFormat = TIME_PARSER): LocalDateTime = format.parseLocalDateTime(this)

/**
 * Formats this date to string.
 */
fun ReadableInstant.format(format: TimeFormat = TIME_FORMAT): String = format.print(this)
fun ReadablePartial.format(format: TimeFormat = TIME_FORMAT): String = format.print(this)
fun Number.formatDate(format: TimeFormat = TIME_FORMAT): String = format.print(toLong())

/**
 * Adds the given duration to this date.
 */
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = plus(duration.toJodaDuration())
operator fun DateTime.plus(duration: Duration): DateTime = plus(duration.toJodaDuration())
operator fun LocalTime.plus(duration: Duration): LocalTime = plus(duration.toPeriod())

/**
 * Subtracts the given duration to this date.
 */
operator fun LocalDateTime.minus(duration: Duration): LocalDateTime = minus(duration.toJodaDuration())
operator fun DateTime.minus(duration: Duration): DateTime = minus(duration.toJodaDuration())
operator fun LocalTime.minus(duration: Duration): LocalTime = minus(duration.toPeriod())

/**
 * Gets if this date is a leap year.
 */
val LocalDateTime.isLeapYear get() = year().isLeap
val DateTime.isLeapYear get() = year().isLeap

/**
 * Gets the year of this date as text.
 */
fun LocalDateTime.yearAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) year().getAsShortText(locale) else year().getAsText(locale)
}

/**
 * Gets the month of this date as text.
 */
fun LocalDateTime.monthAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) monthOfYear().getAsShortText(locale) else monthOfYear().getAsText(locale)
}

/**
 * Gets the day of year of this date as text.
 */
fun LocalDateTime.dayOfYearAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) dayOfYear().getAsShortText(locale) else dayOfYear().getAsText(locale)
}

/**
 * Gets the day of week of this date as text.
 */
fun LocalDateTime.dayOfWeekAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) dayOfWeek().getAsShortText(locale) else dayOfWeek().getAsText(locale)
}

/**
 * Gets the day of month of this date as text.
 */
fun LocalDateTime.dayOfMonthAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) dayOfMonth().getAsShortText(locale) else dayOfMonth().getAsText(locale)
}

/**
 * Gets the hours of day of this date as text.
 */
fun LocalDateTime.hoursAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) hourOfDay().getAsShortText(locale) else hourOfDay().getAsText(locale)
}

/**
 * Gets the minutes of hour of this date as text.
 */
fun LocalDateTime.minutesAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) minuteOfHour().getAsShortText(locale) else minuteOfHour().getAsText(locale)
}

/**
 * Gets the second of minutes of this date as text.
 */
fun LocalDateTime.secondAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) secondOfMinute().getAsShortText(locale) else secondOfMinute().getAsText(locale)
}

/**
 * Gets the millis of day of this date as text.
 */
fun LocalDateTime.millisOfDayAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) millisOfDay().getAsShortText(locale) else millisOfDay().getAsText(locale)
}

/**
 * Gets the millis of second of this date as text.
 */
fun LocalDateTime.millisOfSecondAsText(short: Boolean = false, locale: Locale? = null): String {
  return if (short) millisOfSecond().getAsShortText(locale) else millisOfSecond().getAsText(locale)
}
