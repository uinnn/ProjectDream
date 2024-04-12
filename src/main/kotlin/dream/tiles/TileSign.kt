package dream.tiles

import dream.chat.*
import dream.collections.*
import dream.entity.player.*
import dream.misc.*
import dream.nbt.types.*
import dream.utils.*
import kotlinx.serialization.*

/**
 * A sign tile.
 */
class TileSign : Tile(Tiles.SIGN) {
  var texts: MutableList<SignComponent> = sizedListOf(4)

  override fun onInteract(player: Player, type: Click) {
    for ((line, text) in texts.withIndex()) {
      val click = text.clickEvent ?: continue
      when (click.action) {
        SignClickAction.RUN_COMMAND -> TODO("run command")
        SignClickAction.CHANGE_LINE -> {
          texts[line] = click.value.parseJson()
          setChanged()
        }
      }
    }
  }

  override fun saveAdditional(tag: CompoundTag) {
    tag["Lines"] = texts.toTagAsJson()
  }

  override fun load(tag: CompoundTag) {
    texts = tag.wrapJsonList("Lines")
  }
}

/**
 * Represents a sign component text.
 */
@Serializable
data class SignComponent(var text: String, var clickEvent: SignClickEvent? = null) : BaseComponent() {
  override val unformattedText: String get() = text

  fun run(value: String): SignComponent {
    clickEvent = SignClickEvent(SignClickAction.RUN_COMMAND, value)
    return this
  }

  fun changeLine(value: String): SignComponent {
    clickEvent = SignClickEvent(SignClickAction.CHANGE_LINE, value)
    return this
  }

  override fun copy() = ComponentText(text).also {
    it.style = style.shallowCopy()
    childrens.forEach(it::add)
  }
}

/**
 * Represents a sign click event.
 */
@Serializable
data class SignClickEvent(var action: SignClickAction, var value: String)

/**
 * All click actions that can be perfomed when clicking on a sign.
 */
enum class SignClickAction {
  RUN_COMMAND,
  CHANGE_LINE,
}

