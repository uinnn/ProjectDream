package dream.chat

import dream.api.*
import dream.item.*
import dream.serializer.*
import kotlinx.serialization.*

/**
 * Represents a chat hover event.
 */
@Serializable(HoverEventSerializer::class)
data class HoverEvent(var action: HoverAction, var value: String)

/**
 * All actions that's a hover event can show.
 */
enum class HoverAction(override val id: String, val allowedInChat: Boolean = true) : Id {
  SHOW_TEXT("show_text"),
  SHOW_ACHIEVEMENT("show_achievement"),
  SHOW_ITEM("show_item"),
  SHOW_ENTITY("show_entity");

  companion object {
    val values = values()
    val lookup = values.associateBy { it.id }

    /**
     * Gets a hover action by [id].
     */
    fun byId(id: String) = lookup[id] ?: SHOW_TEXT
  }
}

/**
 * Crates a new [HoverEvent] with action of [HoverAction.SHOW_TEXT].
 */
fun TextHover(value: String) = HoverEvent(HoverAction.SHOW_TEXT, value)

/**
 * Crates a new [HoverEvent] with action of [HoverAction.SHOW_ACHIEVEMENT].
 */
fun AchievementHover(value: String) = HoverEvent(HoverAction.SHOW_ACHIEVEMENT, value)

/**
 * Crates a new [HoverEvent] with action of [HoverAction.SHOW_ITEM].
 */
fun ItemHover(value: String) = HoverEvent(HoverAction.SHOW_ITEM, value)

/**
 * Crates a new [HoverEvent] with action of [HoverAction.SHOW_ITEM].
 */
fun ItemHover(item: ItemStack) = ItemHover(item.store().toString())

/**
 * Crates a new [HoverEvent] with action of [HoverAction.SHOW_ENTITY].
 */
fun EntityHover(value: String) = HoverEvent(HoverAction.SHOW_ENTITY, value)
