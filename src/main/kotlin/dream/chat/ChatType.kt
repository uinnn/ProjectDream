package dream.chat

/**
 * Represents all available types of chat message.
 */
enum class ChatType(val id: Byte) {
   
   /**
    * Main chat message.
    */
   CHAT(1),
   
   /**
    * Action bar message.
    */
   ACTION(2),
   
   /**
    * Title message.
    */
   TITLE(3);
   
   /**
    * Verify if this chat type is considered a vanilla chat.
    */
   val isChat: Boolean
      get() = this === CHAT || this === ACTION
   
   companion object {
      
      /**
       * Gets a chat type by id.
       */
      fun byId(id: Byte) = when (id.toInt()) {
         2 -> ACTION
         3 -> TITLE
         else -> CHAT
      }
   }
}
