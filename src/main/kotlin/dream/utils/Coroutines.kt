package dream.utils

import dream.coroutines.*
import kotlinx.coroutines.*
import org.joda.time.*
import org.joda.time.base.*
import kotlin.coroutines.*
import kotlin.time.Duration
import java.util.*

/**
 * Creates a new [CoroutineScope] by the given [context].
 */
fun scope(context: CoroutineContext = MinecraftDispatcher) = CoroutineScope(context)

/**
 * Launches a new coroutine running forever until their cancellation.
 */
fun CoroutineScope.always(
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(context, start) {
   while (true) {
      block()
   }
}

/**
 * Launches a new coroutine running forever while [block] returns true.
 */
fun CoroutineScope.until(
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Boolean,
) = launch(context, start) {
   while (block()) {
   }
}

/**
 * Launches a new coroutine running repeated amount of times.
 */
fun CoroutineScope.repeated(
   repeat: Int,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.(index: Int) -> Unit,
) = launch(context, start) {
   repeat(repeat) {
      block(it)
   }
}

/**
 * Launches a new coroutine running repeated amount of times like a countdown.
 */
fun CoroutineScope.countdown(
   times: Int,
   downTo: Int = 0,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.(index: Int) -> Unit,
) = launch(context, start) {
   dream.utils.countdown(times, downTo) {
      block(it)
   }
}

/**
 * Launches a new coroutine running [block] after [delay]
 */
fun CoroutineScope.after(
   delay: Long,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(context, start) {
   delay(delay)
   block()
}

/**
 * Launches a new coroutine running [block] after [delay]
 */
fun CoroutineScope.after(
   delay: Duration,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(context, start) {
   delay(delay)
   block()
}

/**
 * Launches a new coroutine running [block] in given [date].
 */
fun CoroutineScope.launch(
   date: Date,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(context, start) {
   delay(date.time - now)
   block()
}

/**
 * Launches a new coroutine running [block] in given [date].
 */
fun CoroutineScope.launch(
   date: AbstractInstant,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(context, start) {
   delay(date.millis - now)
   block()
}

/**
 * Launches a new coroutine running [block] in given [date].
 */
fun CoroutineScope.launch(
   date: LocalDateTime,
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit,
) = launch(date.toDate(), context, start, block)
