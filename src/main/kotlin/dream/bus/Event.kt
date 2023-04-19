package dream.bus

import dream.interfaces.*
import dream.misc.*

@Open
public class Event(
   /**
    * Determinates if this event is allowed to be cancelled.
    */
   var isCancellable: Boolean = false,
   name: String? = null,
) : Nameable {
   
   /**
    * Determinates if this event is cancelled or not.
    *
    * Cancelling an event that [isCancellable] returns false, cause no effect.
    */
   var isCancelled = false
   
   override val name = name ?: this::class.simpleName ?: "Unknown Event"
   
   /**
    * Post this event to the specific [bus].
    */
   public fun post(bus: Bus = Bus) {
      if (isCancellable && isCancelled)
         return
      
      bus.post(this)
   }
}
