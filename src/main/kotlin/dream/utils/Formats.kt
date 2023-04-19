package dream.utils

import kotlin.time.*
import java.text.*

/**
 * Default decimal format with a maximum fraction digits of 2.
 */
val DecimalFormatter by lazy {
   DecimalFormat("##,###").apply { maximumFractionDigits = 2 }
}

/**
 * Default tree map that's corresponds all suffixes to format.
 */
val FormatSuffixes by lazy {
   treeMapOf(
      1E3 to "K",
      1E6 to "M",
      1E9 to "B",
      1e12 to "T",
      1e15 to "Q",
      1e18 to "QQ",
      1e21 to "S",
      1e24 to "SS",
      1E27 to "O",
      1e30 to "N",
      1e33 to "D",
      1e36 to "UN",
      1e39 to "DD",
      1e42 to "TRD",
      1e45 to "QD",
      1e48 to "QND",
      1e51 to "SD",
      1e54 to "SPD",
      1e57 to "OCD",
      1e60 to "UND",
      1e63 to "VG"
   )
}

/**
 * Formats this number spaced-like.
 *
 * Example: `1.000`, `15.591`
 */
fun Number.spaced(): String = DecimalFormatter.format(toDouble())

/**
 * Formats this number spaced-like with suffixes.
 *
 * Example: `1K`, `15.59K`
 */
fun Number.format(): String {
   val value = toDouble()
   if (value < 1000)
      return value.toString()
   
   val entry = FormatSuffixes.floorEntry(value)
   val number = value / (entry.key / 10) / 10
   return number.spaced() + entry.value
}

/**
 * Formats this number parsing as duration and formatting later.
 */
fun Number.formatTime(unit: DurationUnit = DurationUnit.MILLISECONDS): String {
   return toLong().toDuration(unit).format()
}

/**
 * Formats this duration as human-readable.
 */
fun Duration.format(): String {
   val isNegative = isNegative()
   return buildString {
      if (isNegative) append('-')
      absoluteValue.toComponents { days, hours, minutes, seconds, nanoseconds ->
         val hasDays = days != 0L
         val hasHours = hours != 0
         val hasMinutes = minutes != 0
         val hasSeconds = seconds != 0 || nanoseconds != 0
         var components = 0
         
         if (hasDays) {
            append(days).append('d')
            components++
         }
         
         if (hasHours || (hasDays && (hasMinutes || hasSeconds))) {
            if (components++ > 0) append(' ')
            append(hours).append('h')
         }
         
         if (hasMinutes || (hasSeconds && (hasHours || hasDays))) {
            if (components++ > 0) append(' ')
            append(minutes).append('m')
         }
         
         if (hasSeconds) {
            if (components++ > 0) append(' ')
            append(seconds).append('s')
         }
         
         if (isNegative && components > 1) insert(1, '(').append(')')
      }
   }
}
