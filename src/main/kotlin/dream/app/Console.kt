@file:Suppress("NOTHING_TO_INLINE")

package dream.app

import dream.chat.Component
import dream.chat.text
import dream.command.CommandSender
import dream.command.ResultStatsType
import dream.entity.base.Entity
import dream.pos.Pos
import dream.server.Server

/**
 * Represents the console sender.
 */
object Console : CommandSender {

  /**
   * All messages that's the console received.
   */
  val logs = StringBuffer()

  /**
   * The name of the console.
   */
  override val name = "Console"

  /**
   * The display name of the console.
   */
  override val displayName = text(name)

  /**
   * Returns if this sender should be notified about any executed commands.
   */
  override var sendFeedback = true

  override val pos get() = Pos.ZERO
  override val level get() = Server.get().mainLevel
  override val entity: Entity? get() = null

  override fun message(component: Component) {
    val text = component.unformattedText
    logs.append(text)
    println(text)
  }

  override fun canUse(permLevel: Int, commandName: String): Boolean {
    return true
  }

  override fun setCommandStats(type: ResultStatsType, amount: Int) {
  }
}

/**
 * Logs a message to the console
 */
inline fun message(component: Component) = Console.message(component)

/**
 * Logs a message to the console
 */
inline fun message(text: String) = Console.message(text)
