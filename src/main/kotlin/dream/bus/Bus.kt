package dream.bus

import dream.misc.*
import dream.utils.*
import kotlin.reflect.*

typealias Subscribers = MutableMap<KClass<*>, MutableSet<Any.() -> Unit>>

/**
 * Represents a event bus.
 *
 * This allow to handle event actions
 */
@Open
public class Bus {
   
   /**
    * The default bus implementation
    */
   companion object Default : Bus()
   
   /**
    * All subscribers of this bus.
    */
   public val subscribers: Subscribers = HashMap()
   
   /**
    * Register a new event handler for the specific [T] event.
    */
   public final inline fun <reified T : Any> register(noinline action: T.() -> Unit) {
      subscribers.putIfAbsent(T::class, LinkedHashSet())
      subscribers[T::class]!!.add(action.cast())
   }
   
   /**
    * Unregister all event handler for the specific [T] event.
    */
   public final inline fun <reified T : Any> unregisterAll() {
      subscribers -= T::class
   }
   
   /**
    * Post [value] to be processed by [subscribers]
    */
   public fun post(value: Any) {
      val listeners = subscribers[value::class] ?: return
      for (listener in listeners) {
         listener(value)
      }
   }
}
