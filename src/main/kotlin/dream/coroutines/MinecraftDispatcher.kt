package dream.coroutines

import kotlinx.coroutines.*

/**
 * Represennts a [CoroutineDispatcher] used on Minecraft.
 */
object MinecraftDispatcher : BaseDispatcher("Minecraft Thread", 4)
