package dream.inventory

/**
 * Represents all click mode when clicking on container.
 */
enum class ClickMode(val id: Int) {
   MOUSE(0),
   SHIFT_MOUSE(1),
   HOTBAR(2),
   MIDDLE(3),
   DROP(4),
   DRAG(5),
   DOUBLE_CLICK(6);
   
   /**
    * Gets if this click mode is a mouse click.
    */
   val isMouse: Boolean
      get() = this != HOTBAR && this != DROP
   
   /**
    * Gets if this click mode is performed by hotbar.
    */
   val isHotbar: Boolean
      get() = !isMouse
   
   companion object {
      private val values = values()
      
      /**
       * Gets a click mode by [id].
       */
      fun byId(id: Int) = values[id % 7]
   }
}
