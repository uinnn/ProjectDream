@file:Suppress("NOTHING_TO_INLINE")

package dream.app

import dream.chat.*
import dream.command.*
import dream.entity.base.*
import dream.pos.*

/**
 * Represents the console sender.
 */
object Console : CommandSender {

  /**
   * All messages that's the console received.
   */
  val logs = StringBuffer()

  override val name = "Console"
  override val displayName = text(name)
  override val pos = Pos.ZERO
  override val level = TODO("insert the main server world.")
  override val entity: Entity? = null
  override var sendFeedback = true

  override fun message(component: Component) {
    logs.append(component.unformattedText)
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
