package dream.coroutines

import kotlinx.coroutines.*

/**
 * Represents a [CoroutineDispatcher] used on entities.
 */
object EntityDispatcher : BaseDispatcher("Entities Thread", 4)
