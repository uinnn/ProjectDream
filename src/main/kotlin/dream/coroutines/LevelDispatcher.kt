package dream.coroutines

import kotlinx.coroutines.*

/**
 * Represennts a [CoroutineDispatcher] used on levels.
 */
object LevelDispatcher : BaseDispatcher("Levels Thread", 4)
