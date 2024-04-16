package dream.level.storage

import dream.entity.player.Player
import dream.nbt.types.CompoundTag
import java.io.File

interface PlayerStorage {

  /**
   * Returns an list of usernames for which player.dat exists for.
   */
  val availablePlayerDatas: List<String>

  /**
   * Writes the player data to disk from the specified player.
   */
  fun write(player: Player)

  /**
   * Reads the player data from disk into the specified player.
   */
  fun read(player: Player): CompoundTag

  /**
   * Gets the file storage for the specified player.
   */
  fun getPlayerFile(player: Player): File


}
