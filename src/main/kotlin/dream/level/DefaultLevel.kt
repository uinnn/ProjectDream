package dream.level

import dream.api.*
import dream.coroutines.*
import dream.level.provider.*
import dream.level.storage.*
import kotlin.coroutines.*

/**
 * Represents a Level.
 */
abstract class DefaultLevel(
  name: String,
  provider: LevelProvider,
  saveHandler: ISaveHandler,
) : Level(name, provider, saveHandler), Scope {
  override var coroutineContext: CoroutineContext = LevelDispatcher
}
