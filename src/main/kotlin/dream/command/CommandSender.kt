package dream.command

import dream.app.*
import dream.chat.*
import dream.entity.base.*
import dream.level.*
import dream.pos.*

/**
 * Represents a sender of a command.
 */
interface CommandSender {

  /**
   * Gets the name of this sender.
   */
  val name: String

  /**
   * Gets the formatted name of this sender. Used to display them.
   */
  val displayName: Component

  /**
   * Returns the position of this sender.
   */
  val pos: Pos

  /**
   * Returns the level of this sender.
   */
  val level: Level

  /**
   * Returns the entity associated with this sender or nulls
   * if this sender is the console.
   */
  val entity: Entity?

  /**
   * Returns if this sender should be notified about any executed commands.
   */
  var sendFeedback: Boolean

  /**
   * Sends a message to this sender by the given [component].
   */
  fun message(component: Component)

  /**
   * Sends a message to this sender by the given [message].
   */
  fun message(message: String) = message(text(message))

  /**
   * Returns if this sender can use a command.
   */
  fun canUse(permLevel: Int, commandName: String): Boolean

  /**
   * Sets a command stats for this sender.
   *
   * If is a console, do nothing.
   */
  fun setCommandStats(type: ResultStatsType, amount: Int)
}

/**
 * Gets if this command sender is the console.
 */
inline val CommandSender.isConsole: Boolean
  get() = this is Console

