package dream.chat

import dream.api.Id
import dream.serializer.ClickEventSerializer
import kotlinx.serialization.Serializable

/**
 * Represents a chat click event.
 */
@Serializable(ClickEventSerializer::class)
data class ClickEvent(var action: ClickAction, var value: String)

/**
 * All actions that's a click event can do.
 */
enum class ClickAction(override val id: String, val allowedInChat: Boolean = true) : Id {
  OPEN_URL("open_url"),
  RUN_COMMAND("run_command"),
  SUGGEST_COMMAND("suggest_command"),
  CHANGE_PAGE("change_page"),
  OPEN_FILE("open_file", false),
  TWITCH_USER_INFO("twitch_user_info", false);

  companion object {
    val values = values()
    val lookup = values.associateBy { it.id }

    /**
     * Gets a click action by [id].
     */
    fun byId(id: String) = lookup[id] ?: SUGGEST_COMMAND
  }
}

/**
 * Crates a new [ClickEvent] with action of [ClickAction.OPEN_URL].
 */
fun OpenUrlClick(value: String) = ClickEvent(ClickAction.OPEN_URL, value)

/**
 * Crates a new [ClickEvent] with action of [ClickAction.RUN_COMMAND].
 */
fun RunCommandClick(value: String) = ClickEvent(ClickAction.RUN_COMMAND, value)

/**
 * Crates a new [ClickEvent] with action of [ClickAction.SUGGEST_COMMAND].
 */
fun SuggestCommandClick(value: String) = ClickEvent(ClickAction.SUGGEST_COMMAND, value)

/**
 * Crates a new [ClickEvent] with action of [ClickAction.CHANGE_PAGE].
 */
fun ChangePageClick(value: String) = ClickEvent(ClickAction.CHANGE_PAGE, value)

/**
 * Crates a new [ClickEvent] with action of [ClickAction.OPEN_FILE].
 */
fun OpenFileClick(value: String) = ClickEvent(ClickAction.OPEN_FILE, value)

/**
 * Crates a new [ClickEvent] with action of [ClickAction.TWITCH_USER_INFO].
 */
fun TwitchInfoClick(value: String) = ClickEvent(ClickAction.TWITCH_USER_INFO, value)
