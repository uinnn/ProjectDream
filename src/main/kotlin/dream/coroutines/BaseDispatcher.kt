package dream.coroutines

import dream.errors.*
import dream.misc.*
import dream.utils.*
import kotlinx.coroutines.*
import java.util.concurrent.*
import kotlin.coroutines.*

/**
 * A base dispatcher for all dispatchers implemented.
 */
@OptIn(InternalCoroutinesApi::class)
@Open
abstract class BaseDispatcher(name: String, threads: Int = 1) : ExecutorCoroutineDispatcher(), Delay {

  /**
   * The default dispatcher used to delegation.
   */
  val dispatcher = newFixedThreadPoolContext(threads, name)

  /**
   * The executor.
   */
  override val executor: Executor = dispatcher.executor

  /**
   * Gets [executor] as [ScheduledExecutorService].
   */
  val service: ScheduledExecutorService?
    get() = executor as? ScheduledExecutorService

  override fun dispatch(context: CoroutineContext, block: Runnable) {
    catchCrash {
      dispatcher.dispatch(context, block)
    }
  }

  override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
    val future = service?.schedule(continuation.context, timeMillis) {
      with(continuation) {
        resumeUndispatched(Unit)
      }
    }

    if (future != null) {
      continuation.cancelFutureOnCancellation(future)
    }
  }

  override fun invokeOnTimeout(timeMillis: Long, block: Runnable, context: CoroutineContext): DisposableHandle {
    val future = service?.schedule(context, timeMillis, block)
    return DisposableHandle {
      future?.cancel(false)
    }
  }

  override fun close() {
    dispatcher.close()
  }
}

/**
 * Creates a new dispatcher.
 */
fun dispatcher(name: String, threads: Int = 1): BaseDispatcher {
  return object : BaseDispatcher(name, threads) {}
}
