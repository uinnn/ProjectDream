package dream.entity.player

import dream.api.*
import dream.chat.*

/**
 * All chat visibility configurable by the client.
 */
enum class ChatVisibility(override val unlocalName: String) : Locale {
  FULL("full"),
  SYSTEM("system"),
  HIDDEN("hidden");

  val component = text("options.chat.visibility.$unlocalName")

  companion object {

    /**
     * Gets a chat visibility option by id.
     */
    fun byId(id: Int): ChatVisibility {
      return when (id) {
        1 -> SYSTEM
        2 -> HIDDEN
        else -> FULL
      }
    }
  }
}
