package dream.interfaces

import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * A coroutine scope with mutable context.
 */
interface Scope : CoroutineScope {
   override var coroutineContext: CoroutineContext
}
