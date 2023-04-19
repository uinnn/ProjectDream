package dream.utils

import com.soywiz.klock.DateTime
import kotlinx.coroutines.*
import org.joda.time.*
import org.joda.time.base.*
import kotlin.coroutines.*
import java.util.Date
import java.util.concurrent.*

/**
 * Schedules [block] in async.
 */
fun async(block: () -> Unit): CompletableFuture<Void> {
   return CompletableFuture.runAsync(block)
}

/**
 * Schedules [block] in async.
 */
fun <T> supply(block: () -> T): CompletableFuture<T> {
   return CompletableFuture.supplyAsync(block)
}

/**
 * Schedules [block] in this [ScheduledExecutorService].
 *
 * If [RejectedExecutionException] is thrown,
 * will cancel the coroutine [context] and returns null.
 */
fun ScheduledExecutorService.schedule(
   context: CoroutineContext,
   timeMillis: Long,
   block: Runnable,
): ScheduledFuture<*>? {
   return try {
      schedule(block, timeMillis, TimeUnit.MILLISECONDS)
   } catch (e: RejectedExecutionException) {
      context.cancel()
      null
   }
}

/**
 * Schedules [block] to be executed in the given [date].
 */
fun ScheduledExecutorService.schedule(date: Date, block: () -> Unit): ScheduledFuture<*> {
   return schedule(block, date.time, TimeUnit.MILLISECONDS)
}

/**
 * Schedules [block] to be executed in the given [date].
 */
fun ScheduledExecutorService.schedule(date: DateTime, block: () -> Unit): ScheduledFuture<*> {
   return schedule(block, date.unixMillisLong, TimeUnit.MILLISECONDS)
}

/**
 * Schedules [block] to be executed in the given [date].
 */
fun ScheduledExecutorService.schedule(date: AbstractInstant, block: () -> Unit): ScheduledFuture<*> {
   return schedule(block, date.millis, TimeUnit.MILLISECONDS)
}

/**
 * Schedules [block] to be executed in the given [date].
 */
fun ScheduledExecutorService.schedule(date: LocalDateTime, block: () -> Unit): ScheduledFuture<*> {
   return schedule(date.toDate(), block)
}
