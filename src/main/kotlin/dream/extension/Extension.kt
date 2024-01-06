package dream.extension

import dream.api.*
import dream.coroutines.*
import dream.errors.*
import dream.misc.*
import kotlin.coroutines.*
import kotlin.reflect.full.*

/**
 * Represents a basic extension.
 *
 * Extension is a way to create/handling custom scenarios in a Minecraft Server.
 *
 * `a.k.a. Plugin`
 */
@Open
abstract class Extension : Scope {
  override var coroutineContext: CoroutineContext = MinecraftDispatcher

  /**
   * The module of this plugin.
   */
  val module: Module = findModule()

  /**
   * Called when this plugin is loading.
   *
   * Can be used to manipulate infrastructure of the server.
   */
  fun onLoad() = Unit

  /**
   * Called when this plugin is starting to stable execution.
   */
  fun onStart() = Unit

  /**
   * Called when this plugin is disabling to stop completely
   * the execution of the plugin in the server.
   */
  fun onDisable() = Unit

  /**
   * Tries to find the [Module] of this plugin.
   */
  protected fun findModule(): Module {
    return this::class.findAnnotation()
      ?: throw ModuleNotFoundException("No extension module found for class ${this::class}")
  }
}
