package dream.inventory

/**
 * Represents all drag events when dragging an item on a container.
 */
enum class DragEvent(val id: Int) {
   
   /**
    * Starting the drag.
    */
   START(0),
   
   /**
    * Adding all slots of the dragging.
    */
   ADD_SLOT(1),
   
   /**
    * Dragging ended.
    */
   END(2);
   
   companion object {
      
      /**
       * Gets a drag event by [id].
       */
      fun byId(id: Int) = when (id and 3) {
         1 -> ADD_SLOT
         2 -> END
         else -> START
      }
   }
}
