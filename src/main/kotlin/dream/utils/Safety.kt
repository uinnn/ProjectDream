package dream.utils

import java.text.SimpleDateFormat
import java.util.*

/*
 *
 * this file contains safety extensions functions
 * to be parsed as null instead throwing exceptions
 *
 */

fun SimpleDateFormat.parseOrNull(value: String?): Date? {
  return value?.let {
    catchingOrNull {
      parse(it)
    }
  }
}

fun String?.toUUID(): UUID? {
  if (this == null) return null
  return catchingOrNull {
    uuid(this)
  }
}
