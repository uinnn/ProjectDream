package dream.command

/**
 * Represents a base interface for any command.
 */
interface ICommand : Comparable<ICommand> {

  /**
   * Gets the name of the command.
   */
  val name: String

  /**
   * Gets the aliases of the command.
   */
  val aliases: MutableList<String>

  /**
   * Gets the usage text for the command.
   */
  fun getUsage(sender: CommandSender): String

  /**
   * Process the command.
   *
   * Called when this command is executed from a player or console.
   */
  fun process(sender: CommandSender, args: Array<String>)

  /**
   * Returns if the given [sender] is allowed to use this command.
   */
  fun canUse(sender: CommandSender): Boolean

  override fun compareTo(other: ICommand): Int {
    return name.compareTo(other.name)
  }
}
