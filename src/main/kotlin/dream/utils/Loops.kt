package dream.utils

import kotlin.contracts.*
import java.util.*

/**
 * Executes the given function [action] specified number of [times].
 *
 * Working like a countdown, this will be start on index [times] until [downTo].
 */
inline fun countdown(times: Int, downTo: Int = 0, action: (Int) -> Unit) {
   contract { callsInPlace(action) }
   
   for (index in times downTo downTo) {
      action(index)
   }
}

/**
 * Executes [action] for every element and removes them.
 *
 * This will start from the first element to the last element.
 */
inline fun <T> MutableList<T>.forEachRemove(action: (T) -> Unit) {
   for (value in this) {
      action(value)
      remove(value)
   }
}

/**
 * Executes [action] for every element and removes them.
 *
 * This will start from the last element to the first element.
 */
inline fun <T> MutableList<T>.forEachPoll(action: (T) -> Unit) {
   var n = size
   while (n >= 0) {
      val element = get(n)
      action(element)
      remove(element)
      n--
   }
}

/**
 * Executes [action] for every element and removes them.
 *
 * This will start from the first element to the last element.
 */
inline fun <T> Queue<T>.forEachRemove(action: (T) -> Unit) {
   for (value in this) {
      action(value)
      remove(value)
   }
}

/**
 * Executes [action] for every element and removes them.
 *
 * This will start from the last element to the first element.
 */
inline fun <T> Queue<T>.forEachPoll(action: (T) -> Unit) {
   while (isNotEmpty()) action(poll())
}
